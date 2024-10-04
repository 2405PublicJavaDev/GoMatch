package com.moijo.gomatch.app.meeting;

import com.moijo.gomatch.common.FileUtil;
import com.moijo.gomatch.domain.game.vo.GameVO;
import com.moijo.gomatch.domain.meeting.service.MeetingService;
import com.moijo.gomatch.domain.meeting.vo.MeetingVO;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

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
        return "redirect:/meeting/meeting"; // 소모임 목록 페이지로 리다이렉트
    }

    @GetMapping("/meeting/list")
    public String showMeetingList(Model model) {
        // 예시 팀 이름 리스트를 모델에 추가
        model.addAttribute("teams", List.of("KIA 타이거즈", "롯데 자이언츠", "삼성 라이온즈", "두산베어스", "KT 위즈", "SSG랜더스", "NC 다이노스", "한화 이글스", "키움 히어로즈", "LG 트윈스"));
        // 현재 날짜의 소모임 리스트를 모델에 추가 (기본 조회)
        String today = LocalDate.now().toString();
        List<MeetingVO> meetings = meetingService.getMeetingsByDate(today);
        model.addAttribute("meetings", meetings);
        return "meeting/meeting-list";
    }

    @GetMapping("/meeting/listByDate")
    @ResponseBody
    public List<MeetingVO> getMeetingsByDate(@RequestParam("date") String date) {
        return meetingService.getMeetingsByDate(date);

    }

    @GetMapping("/meeting/listByDateAndTeam")
    @ResponseBody
    public List<MeetingVO> getMeetingsByDateAndTeam(@RequestParam("date") String date,
                                                    @RequestParam("team") String team) {
        return meetingService.getMeetingsByDateAndTeam(date, team);
    }

}