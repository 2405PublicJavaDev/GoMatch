package com.moijo.gomatch.app.meeting;

import com.moijo.gomatch.domain.meeting.service.MeetingBoardService;
import com.moijo.gomatch.domain.member.vo.MemberVO;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


@Controller
@RequiredArgsConstructor
@Slf4j
public class MeetingBoardController {
    private final MeetingBoardService meetingBoardService;


    @GetMapping("/board/register")
    public String showAddBoardPage(HttpSession session, Model model) {
        String memberId = (String) session.getAttribute("memberId");
        String memberNickName = (String) session.getAttribute("memberNickName");
        if (memberId == null) {
            return "로그인이 필요합니다.";
        }else {
            model.addAttribute("loggedIn", true);
            model.addAttribute("memberNickName", memberNickName);
        }
        return "board/board-register";
    }
}
