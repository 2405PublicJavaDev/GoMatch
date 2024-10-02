package com.moijo.gomatch.app.meeting;

import com.moijo.gomatch.domain.game.vo.GameVO;
import com.moijo.gomatch.domain.meeting.service.MeetingService;
import com.moijo.gomatch.domain.meeting.vo.MeetingVO;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MeetingController {

    private final MeetingService meetingService;

    /**
     * 담당자 : 김윤경
     * 관련 기능 : [페이지 폼] 소모임 개설하기
     * 설명 : 소모임 개설 페이지 보여주기
     */
//    @GetMapping("/meeting/register")
//    public String showAddMeetingPage(HttpSession session, Model model) {
//        String memberId = (String) session.getAttribute("memberId");
//        model.addAttribute("games", List.of());  // 경기 정보가 없을 때 빈 리스트
//        return "meeting/meeting-register";
//    }
    @GetMapping("/meeting/register")
    public String showAddMeetingPage(HttpSession session, Model model) {
        String memberId = (String) session.getAttribute("memberId");
        if (memberId == null) {
            System.out.println("memberId가 세션에 없음");
        } else {
            System.out.println("세션에서 가져온 memberId: " + memberId);
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
//    @PostMapping("/meeting/register")
//    public String addMeeting(MeetingVO meetingVO, HttpSession session) {
//        String memberId = (String) session.getAttribute("memberId");
//        if (memberId == null) {
//            return "redirect:/login";
//        }
//        // 세션에서 회원 ID를 설정
//        meetingVO.setMemberId(memberId);
//        // 소모임 등록 서비스 호출
//        meetingService.addMeeting(meetingVO);
//        return "redirect:/meeting/meeting";
//    }

    @PostMapping("/meeting/register")
    public String addMeeting(MeetingVO meetingVO, HttpSession session) {
        // 테스트용으로 세션에 임시 사용자 정보 설정
        String memberId = (String) session.getAttribute("memberId");
        if (memberId == null) {
            // 테스트 중 로그인 구현이 안 되어 있으므로 임의의 사용자 ID를 설정
            memberId = "user9"; // 임시 회원 ID
            session.setAttribute("memberId", memberId);
        }
        // 소모임 정보를 등록하는 코드
        meetingVO.setMemberId(memberId);
        meetingService.addMeeting(meetingVO);
        return "redirect:/meeting/meeting"; // 소모임 목록 페이지로 리다이렉트
    }

}
