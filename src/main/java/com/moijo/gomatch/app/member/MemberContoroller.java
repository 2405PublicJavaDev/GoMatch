package com.moijo.gomatch.app.member;

import com.moijo.gomatch.domain.member.service.MemberService;
import com.moijo.gomatch.domain.member.vo.MemberVO;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class MemberContoroller {

    private final MemberService mService;


    // 로그인 폼
    @GetMapping("member/loginpage")
    public String showLoginPage(Model model) {
        return "member/loginpage";
    }

    // 로그인 기능
    @PostMapping("member/loginpage")
    public String loginpage(@RequestParam("memberId") String memberId,
                            @RequestParam("memberPw") String memberPw, HttpSession session) {
        MemberVO member = new MemberVO();

        member.setMemberId(memberId);
        member.setMemberPw(memberPw);

        member = mService.checkLogin(member);
        if (member != null) {
            session.setAttribute("member", member);
            session.setAttribute("memberId", memberId);
            session.setAttribute("memberNickName", member.getMemberNickName());
            session.setAttribute("matchPredictExp", member.getMatchPredictExp());
            session.setAttribute("memberEmail", member.getMemberEmail());
            System.out.println("member" + member);
        }
        return "redirect:/";
    }
//        // 로그인 성공 또는 실패 처리 (예: 로그인 실패 시 에러 메시지 추가)
//        if (member != null) {
//            // 로그인 성공 처리
//            return "redirect:/";  // 예시로 대시보드로 리다이렉트
//        } else {
//            // 로그인 실패 처리
//            model.addAttribute("loginError", "아이디 또는 비밀번호가 올바르지 않습니다.");
//            return "member/loginpage";
//        }



    // 회원가입 폼 ;
    @GetMapping("member/joinmember")
    public String showJoinPage(Model model) {
        return "member/joinmember";
    }

//    // 회원가입 기능
//    @PostMapping("member/joinmember")
//    public String joinMember(MemberVO member){
//        mService.joinMember(member);
//
//        return "redirect:/member/loginpage";
//    }







}
