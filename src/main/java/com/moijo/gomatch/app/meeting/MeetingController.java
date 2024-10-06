package com.moijo.gomatch.app.meeting;

import com.moijo.gomatch.common.FileUtil;
import com.moijo.gomatch.domain.game.vo.GameVO;
import com.moijo.gomatch.domain.meeting.service.MeetingService;
import com.moijo.gomatch.domain.meeting.vo.MeetingAttendVO;
import com.moijo.gomatch.domain.meeting.vo.MeetingFileVO;
import com.moijo.gomatch.domain.meeting.vo.MeetingVO;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MeetingController {

    private final MeetingService meetingService;
    private final FileUtil fileUtil;

    /**
     * 담당자 : 김윤경
     * 관련 기능 : [페이지 폼] 소모임 개설하기
     * 설명 : 소모임 개설 페이지 보여주기
     */
    @GetMapping("/meeting/register")
    public String showAddMeetingPage(HttpSession session, Model model) {
        String memberId = (String) session.getAttribute("memberId");
        if (memberId == null) {
            log.info("memberId가 세션에 없음");
        } else {
            log.info("세션에서 가져온 memberId: " + memberId);
        }
        model.addAttribute("games", List.of());  // 경기 정보가 없을 때 빈 리스트
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
        // 세션에서 사용자 ID 가져오기
        String memberId = (String) session.getAttribute("memberId");
        if (memberId == null) {
            memberId = "user9"; // 테스트용 ID
            session.setAttribute("memberId", memberId);
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
    public String showMeetingList(Model model) {
        // 예시 팀 이름 리스트를 모델에 추가
        model.addAttribute("teams", List.of("기아", "롯데", "삼성", "두산", "KT", "SSG", "NC", "한화", "키움", "LG"));
        // 현재 날짜의 소모임 리스트를 모델에 추가 (기본 조회)
        String today = LocalDate.now().toString();
        List<MeetingVO> meetings = meetingService.getMeetingsByDate(today);
        model.addAttribute("meetings", meetings);
        return "meeting/meeting-list";
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
        if (memberId == null) {
            memberId = "user9"; // 테스트용 ID
            session.setAttribute("memberId", memberId);
        }
        // 소모임 상세 정보, 파일, 참석자 조회
        MeetingVO meetingDetail = meetingService.getMeetingsByMeetingNo(meetingNo);
        List<MeetingFileVO> meetingFile = meetingService.getMeetingFileByMeetingNo(meetingNo);
        List<MeetingAttendVO> meetingAttendee = meetingService.getMeetingAttendeeByMeetingNo(meetingNo);
        // 참석자 수 계산
        int currentAttendeesCount = meetingAttendee.size();
        model.addAttribute("meeting", meetingDetail); // 소모임 정보
        model.addAttribute("meetingFile", meetingFile); // 파일 정보
        model.addAttribute("meetingAttendee", meetingAttendee); // 참석자 정보
        model.addAttribute("currentAttendeesCount", currentAttendeesCount); // 현재 참석자 수
        return "meeting/meeting-detail";
    }
    /**
     * 담당자: 김윤경
     * 관련 기능: [Attend] 소모임 참여
     * 설명: 소모임에 로그인한 사용자를 참석자로 등록
     */
    @PostMapping("/meeting/attend")
    @ResponseBody
    public String attendMeeting(@RequestParam("meetingNo") long meetingNo, HttpSession session) {
        String memberId = (String) session.getAttribute("memberId");
        if (memberId == null) {
            return "로그인이 필요합니다.";
        }
        MeetingAttendVO attendVO = new MeetingAttendVO();
        attendVO.setMeetingNo((int) meetingNo);
        attendVO.setMemberId(memberId);
        attendVO.setMeetingAttendYn("Y");
        boolean isAlreadyAttended = meetingService.checkAlreadyAttended(meetingNo, memberId);
        if (isAlreadyAttended) {
            return "이미 참석한 소모임입니다.";
        }
        meetingService.addAttend(attendVO);
        return "참석이 완료되었습니다.";
    }

}