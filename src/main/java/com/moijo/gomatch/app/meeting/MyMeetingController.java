package com.moijo.gomatch.app.meeting;

import com.moijo.gomatch.domain.meeting.service.MeetingBoardService;
import com.moijo.gomatch.domain.meeting.service.MeetingService;
import com.moijo.gomatch.domain.meeting.vo.*;
import com.moijo.gomatch.domain.member.service.MemberService;
import com.moijo.gomatch.domain.member.vo.MemberVO;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MyMeetingController {
    private final MeetingService meetingService;
    private final MeetingBoardService boardService;

    // 메서드들에 nickname 세션 저장 (헤더부분)
    @ModelAttribute
    public void addAttributes(Model model, HttpSession session) {
        MemberVO member = (MemberVO) session.getAttribute("member");
        if (member != null) {
            model.addAttribute("loggedIn", true);
            model.addAttribute("memberNickName", member.getMemberNickName());
            model.addAttribute("memberVO", member);  // memberVO를 추가하여 템플릿에서 접근 가능하도록 함
            model.addAttribute("memberName", member.getMemberName());
        } else {
            model.addAttribute("loggedIn", false);
        }
    }

    @GetMapping("/my/myMeeting")
    public String showMyMeetingList(Model model, HttpSession session,
                                    @RequestParam(value = "memberId", required = false) String memberId) {
        MemberVO member = (MemberVO) session.getAttribute("member");

        // 세션에 member가 없는 경우 로그인 페이지로 리다이렉트
        if (member == null) {
            return "common/oops";
        }

        if (memberId == null) {
            memberId = member.getMemberId();
        }

        List<MeetingVO> meetings = meetingService.getMeetingsByMemberId(memberId);
        model.addAttribute("meetings", meetings);
        List<MeetingAttendVO> attends = meetingService.getMeetingAttendsByMemberId(memberId);
        model.addAttribute("attends", attends);
        List<MeetingBoardVO> boards = boardService.getBoardsByMemberId(memberId);
        model.addAttribute("boards", boards);
        List<MeetingBoardReplyVO> replies = boardService.getBoardRepliesByMemberId(memberId);
        model.addAttribute("replies", replies);
        List<MeetingBoardLikeVO> likes = boardService.getBoardLikesByMemberId(memberId);
        model.addAttribute("likes", likes);

        return "meeting/my-meeting";
    }
}
