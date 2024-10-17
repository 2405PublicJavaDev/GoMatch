package com.moijo.gomatch.app.meeting;

import com.moijo.gomatch.common.MeetingFileUtil;
import com.moijo.gomatch.domain.game.vo.GameVO;
import com.moijo.gomatch.domain.meeting.service.MeetingService;
import com.moijo.gomatch.domain.meeting.vo.MeetingAttendVO;
import com.moijo.gomatch.domain.meeting.vo.MeetingFileVO;
import com.moijo.gomatch.domain.meeting.vo.MeetingVO;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MeetingController {

    private final MeetingService meetingService;
    private final MeetingFileUtil fileUtil;

    @GetMapping("/meeting/checkLogin")
    @ResponseBody
    public Map<String, Boolean> checkLogin(HttpSession session) {
        Map<String, Boolean> response = new HashMap<>();
        String memberId = (String) session.getAttribute("memberId");
        response.put("loggedIn", memberId != null);
        return response;
    }

    /**
     * 담당자 : 김윤경
     * 관련 기능 : [페이지 폼] 소모임 개설하기
     * 설명 : 소모임 개설 페이지 보여주기
     */
    @GetMapping("/meeting/register")
    public String showAddMeetingPage(HttpSession session, Model model) {
        String memberId = (String) session.getAttribute("memberId");
        String memberNickName = (String) session.getAttribute("memberNickName");
        model.addAttribute("games", List.of());  // 경기 정보가 없을 때 빈 리스트
        if (memberId == null) {
            return "meeting/meeting-list";
        } else {
            model.addAttribute("loggedIn", true);
            model.addAttribute("memberNickName", memberNickName);
        }
        return "meeting/meeting-register";
    }

    /**
     * 담당자 : 김윤경
     * 관련 기능 : [AJAX 페이지 폼] 소모임 개설하기
     * 설명 : 경기 날짜 선택 시 해당 경기 정보 반환
     */
    @GetMapping("/meeting/games")
    @ResponseBody
    public List<GameVO> getGamesByDate(@RequestParam("date") String date) {
        List<GameVO> games = meetingService.getGamesByDate(date);
        System.out.println("경기 수: " + games.size());
        return games; // JSON 형식으로 게임 데이터를 반환
    }

    /**
     * 담당자 : 김윤경
     * 관련 기능 : [Register] 소모임 개설하기
     * 설명 : 세션에 있는 멤버 아이디 불러와서 소모임 등록하기
     */
    @PostMapping("/meeting/register")
    public String addMeeting(MeetingVO meetingVO,
                             @RequestParam("groupImage") List<MultipartFile> groupImages,
                             HttpSession session) throws IOException {
        String memberId = (String) session.getAttribute("memberId");
        if (memberId == null) {
            return "로그인이 필요합니다.";
        } else {
            log.info("세션에서 가져온 memberId: " + memberId);
        }
        // 소모임 정보 등록
        meetingVO.setMemberId(memberId);
        meetingService.addMeeting(meetingVO);
        // 파일 업로드 처리 (파일이 있으면)
        if (!groupImages.isEmpty()) {
            fileUtil.uploadFiles(groupImages, Long.valueOf(meetingVO.getMeetingNo()), "meeting");
        }
        return "redirect:/meeting/detail/" + meetingVO.getMeetingNo(); // 소모임 목록 페이지로 리다이렉트
    }

    /**
     * 담당자 : 김윤경
     * 관련 기능 : [Show] 날짜별 소모임 리스트 출력
     * 설명 : 날짜별 소모임 리스트 출력하기
     */
    @GetMapping("/meeting/list")
    public String showMeetingList(Model model, HttpSession session) {
        String memberId = (String) session.getAttribute("memberId");
        String memberNickName = (String) session.getAttribute("memberNickName");

        // 세션에 memberId가 존재하면 loggedIn을 true로 설정
        boolean loggedIn = memberId != null;
        model.addAttribute("loggedIn", loggedIn);
        model.addAttribute("memberNickName", loggedIn ? memberNickName : ""); // 로그인하지 않았을 경우 빈 문자열

        // 예시 팀 이름 리스트를 모델에 추가
        model.addAttribute("teams", List.of("기아", "롯데", "삼성", "두산", "KT", "SSG", "NC", "한화", "키움", "LG"));

        // 현재 날짜의 소모임 리스트를 모델에 추가 (기본 조회)
        String today = LocalDate.now().toString();
        List<MeetingVO> meetings = meetingService.getMeetingsByDate(today);

        // 참석자 수를 계산하여 모델에 추가
        Map<Long, Integer> attendeesCountMap = new HashMap<>();
        for (MeetingVO meeting : meetings) {
            List<MeetingAttendVO> attendees = meetingService.getMeetingAttendeeByMeetingNo(meeting.getMeetingNo());
            attendeesCountMap.put(meeting.getMeetingNo(), attendees.size());
        }

        model.addAttribute("meetings", meetings);
        model.addAttribute("attendeesCountMap", attendeesCountMap);
        return "meeting/meeting-list";
    }


    @GetMapping("/meeting/gameDates")
    @ResponseBody
    public List<String> getAllGameDates() {
        return meetingService.getAllGameDates();
    }

    /**
     * 담당자 : 김윤경
     * 관련 기능 : [Show] 날짜별 소모임 리스트 출력
     * 설명 : 날짜별, 팀별 필터 적용
     */
    @GetMapping("/meeting/listByDateAndTeam")
    @ResponseBody
    public List<Map<String, Object>> getMeetingsByDateAndTeam(@RequestParam(value = "date", required = false) String date,
                                                              @RequestParam(value = "team", defaultValue = "전체") String team) {
        log.info("필터 적용 - 날짜: {}, 팀: {}", date, team);
        List<MeetingVO> meetings = meetingService.getMeetingsByDateAndTeam(date, team);
        List<Map<String, Object>> responseList = new ArrayList<>();
        for (MeetingVO meeting : meetings) {
            Map<String, Object> meetingData = new HashMap<>();
            meetingData.put("meeting", meeting);
            List<MeetingAttendVO> attendees = meetingService.getMeetingAttendeeByMeetingNo(meeting.getMeetingNo());
            meetingData.put("currentAttendeesCount", attendees.size());  // 참석자 수
            responseList.add(meetingData);
        }
        return responseList;
    }

    /**
     * 담당자 : 김윤경
     * 관련 기능 : [Show] 날짜별 소모임 리스트 출력
     * 설명 : gameNo에 따른 경기 정보 불러오기
     */
    @GetMapping("/meeting/gameInfo")
    @ResponseBody
    public GameVO getGameInfo(@RequestParam("gameNo") int gameNo) {
        return meetingService.getGameByNo(gameNo);
    }

    /**
     * 담당자 : 김윤경
     * 관련 기능 : [Show] 소모임 디테일 페이지
     * 설명 : 소모임 디테일 페이지 출력
     */
    @GetMapping("/meeting/detail/{meetingNo}")
    public String showMeetingDetailPage(@PathVariable("meetingNo") long meetingNo, HttpSession session, Model model) {
        String memberId = (String) session.getAttribute("memberId");
        model.addAttribute("loggedIn", true);
        String memberNickName = (String) session.getAttribute("memberNickName");
        model.addAttribute("memberNickName", memberNickName);
        log.info("세션에서 가져온 memberId: " + memberId);

        // 소모임 상세 정보, 파일, 참석자 조회
        MeetingVO meetingDetail = meetingService.getMeetingsByMeetingNo(meetingNo);
        List<MeetingFileVO> meetingFile = meetingService.getMeetingFileByMeetingNo(meetingNo);
        List<MeetingAttendVO> meetingAttendee = meetingService.getMeetingAttendeeByMeetingNo(meetingNo);

        // 참석자 수 계산
        int currentAttendeesCount = meetingAttendee.size();

        // 현재 날짜와 모임 날짜를 비교하여 지난 모임인지 확인
        LocalDate today = LocalDate.now();
        LocalDate meetingDate;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime dateTime = LocalDateTime.parse(meetingDetail.getMeetingDate(), formatter);
            meetingDate = dateTime.toLocalDate();
        } catch (Exception e) {
            log.error("날짜 파싱 중 오류 발생: " + e.getMessage());
            return "common/oops";
        }

        boolean isPastMeeting = meetingDate.isBefore(today);
        boolean isAttending = meetingService.checkAlreadyAttended(meetingNo, memberId);


        model.addAttribute("meeting", meetingDetail);
        model.addAttribute("isPastMeeting", isPastMeeting);
        model.addAttribute("meetingFile", meetingFile);
        model.addAttribute("meetingAttendee", meetingAttendee);
        model.addAttribute("currentAttendeesCount", currentAttendeesCount);
        model.addAttribute("isAttending", isAttending);

        return "meeting/meeting-detail";
    }


    /**
     * 담당자 : 김윤경
     * 관련 기능 : [Modify] 소모임 수정하기
     * 설명 : 소모임 수정 페이지 보여주기
     */
    @GetMapping("/meeting/modify/{meetingNo}")
    public String showModifyMeetingPage(@PathVariable("meetingNo") long meetingNo, HttpSession session, Model model) {
        String memberId = (String) session.getAttribute("memberId");
        model.addAttribute("loggedIn", true);
        String memberNickName = (String) session.getAttribute("memberNickName");
        model.addAttribute("memberNickName", memberNickName);
        if (memberId == null) {
            return "common/oops";
        }
        MeetingVO meetingDetail = meetingService.getMeetingsByMeetingNo(meetingNo);
        if (!memberId.equals(meetingDetail.getMemberId())) {
            return "common/oops";
        }
        List<MeetingFileVO> meetingFiles = meetingService.getMeetingFileByMeetingNo(meetingNo);
        model.addAttribute("meeting", meetingDetail);
        model.addAttribute("meetingFile", meetingFiles);
        // 경기 번호를 통해 경기 정보를 조회하고 모델에 추가
        GameVO gameInfo = meetingService.getGameByNo(meetingDetail.getGameNo());
        model.addAttribute("gameInfo", gameInfo);
        return "meeting/meeting-modify";
    }

    /**
     * 담당자 : 김윤경
     * 관련 기능 : [Modify] 소모임 수정 처리
     * 설명 : 소모임 정보와 첨부파일을 수정하기
     */
    @PostMapping("/meeting/modify")
    public String modifyMeeting(MeetingVO meetingVO,
                                @RequestParam(value = "newFiles", required = false) List<MultipartFile> newFiles,
                                @RequestParam(value = "fileDeleteIds", required = false) List<Long> fileDeleteIds,
                                HttpSession session) throws IOException {
        log.info("modifyMeeting 메서드 호출됨. 삭제할 파일 ID 목록: {}", fileDeleteIds);

        String memberId = (String) session.getAttribute("memberId");
        if (memberId == null) {
            return "common/oops";
        }
        MeetingVO existingMeeting = meetingService.getMeetingsByMeetingNo(meetingVO.getMeetingNo());
        if (!memberId.equals(existingMeeting.getMemberId())) {
            return "common/oops";
        }
        meetingVO.setMemberId(memberId);
        meetingService.modifyMeeting(meetingVO);
        // 삭제할 파일 확인
        if (fileDeleteIds != null && !fileDeleteIds.isEmpty()) {
            log.info("삭제할 파일 ID 목록: " + fileDeleteIds);
            meetingService.deleteMeetingFiles(fileDeleteIds);
        }
        if (newFiles != null && !newFiles.isEmpty()) {
            fileUtil.uploadFiles(newFiles, meetingVO.getMeetingNo(), "meeting");
        }
        return "redirect:/meeting/detail/" + meetingVO.getMeetingNo();
    }

    /**
     * 담당자 : 김윤경
     * 관련 기능 : [Delete] 소모임 삭제
     * 설명 : 소모임 삭제 기능 추가
     */
    @PostMapping("/meeting/delete/{meetingNo}")
    public String deleteMeeting(@PathVariable("meetingNo") long meetingNo, HttpSession session) {
        String memberId = (String) session.getAttribute("memberId");
        if (memberId == null) {
            return "로그인이 필요합니다.";
        }
        MeetingVO meeting = meetingService.getMeetingsByMeetingNo(meetingNo);
        if (!memberId.equals(meeting.getMemberId())) {
            return "소모임 삭제 권한이 없습니다.";
        }
        meetingService.removeMeeting(meetingNo);
        return "redirect:/meeting/list";
    }

    // ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■ 소모임 참석/취소 (MeetingAttend) ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■ //

    /**
     * 담당자: 김윤경
     * 관련 기능: [Attend] 소모임 참여
     * 설명: 소모임에 로그인한 사용자를 참석자로 등록
     */
    @PostMapping("/meeting/attend")
    @ResponseBody
    public ResponseEntity<Map<String, String>> attendMeeting(@RequestParam("meetingNo") long meetingNo, HttpSession session) {
        String memberId = (String) session.getAttribute("memberId");
        Map<String, String> response = new HashMap<>();

        if (memberId == null) {
            response.put("message", "로그인이 필요한 서비스입니다. 로그인을 하시겠습니까?");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        MeetingAttendVO attendVO = new MeetingAttendVO();
        attendVO.setMeetingNo((int) meetingNo);
        attendVO.setMemberId(memberId);
        attendVO.setMeetingAttendYn("Y");

        boolean isAlreadyAttended = meetingService.checkAlreadyAttended(meetingNo, memberId);
        if (isAlreadyAttended) {
            response.put("message", "이미 참석한 소모임입니다.");
            return ResponseEntity.ok(response);
        }

        meetingService.addAttend(attendVO);
        response.put("message", "참석이 완료되었습니다.");
        return ResponseEntity.ok(response);
    }

    /**
     * 담당자: 김윤경
     * 관련 기능: [Cancel] 소모임 참석 취소
     * 설명: 소모임에 로그인한 사용자의 참석을 취소
     */
    @PostMapping("/meeting/cancel")
    @ResponseBody
    public String cancelMeeting(@RequestParam("meetingNo") long meetingNo, HttpSession session) {
        String memberId = (String) session.getAttribute("memberId");
        if (memberId == null) {
            return "로그인이 필요합니다.";
        }
        // 참석 여부 확인
        boolean isAttended = meetingService.checkAlreadyAttended(meetingNo, memberId);
        if (!isAttended) {
            return "참석하지 않은 모임입니다.";
        }
        // 참석 취소 처리
        meetingService.cancelAttend(meetingNo, memberId);
        return "참석이 취소되었습니다.";
    }


}