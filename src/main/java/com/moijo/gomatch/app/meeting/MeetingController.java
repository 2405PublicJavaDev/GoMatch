package com.moijo.gomatch.app.meeting;

import com.moijo.gomatch.domain.game.vo.GameVO;
import com.moijo.gomatch.domain.meeting.dto.GameDTO;
import com.moijo.gomatch.domain.meeting.service.MeetingService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
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
    @GetMapping("/meeting/register")
    public String showAddMeetingPage(HttpSession session, Model model) {
        String memberId = (String) session.getAttribute("memberId");
        model.addAttribute("games", List.of());  // 경기 정보가 없을 때 빈 리스트
        return "meeting/meeting-register";
    }

    @GetMapping("/meeting/games")
    public String getGamesByDate(@RequestParam("date") LocalDate date, Model model) {
        List<GameVO> games = meetingService.getGamesByDate(date);
        // 경기가 있는지 로그로 확인
        System.out.println("경기 수: " + games.size());
        model.addAttribute("games", games);  // 경기 정보를 모델에 추가
        // 타임리프 Fragment로 경기 정보만 갱신
        return "meeting/meeting-register :: #gameInfo";
    }

    @PostMapping("/meeting/register")
    public String AddMeeting() {
        return "redirect:/meeting/meeting";
    }

}
