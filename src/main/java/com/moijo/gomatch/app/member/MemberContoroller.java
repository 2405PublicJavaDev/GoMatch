package com.moijo.gomatch.app.member;

import com.moijo.gomatch.domain.member.service.MemberService;
import com.moijo.gomatch.domain.member.vo.MemberVO;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

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
                            @RequestParam("memberPw") String memberPw,
                            HttpSession session, Model model) {
        MemberVO member = new MemberVO();
        member.setMemberId(memberId);
        member.setMemberPw(memberPw);

        member = mService.checkLogin(member);
        if (member != null) {
            session.setAttribute("member", member);
            session.setAttribute("memberId", member.getMemberId());
            session.setAttribute("memberNickName", member.getMemberNickName());

            System.out.println("로그인 성공: " + member.getMemberNickName());
            return "redirect:/";
        } else {
            session.setAttribute("loginError", "회원 정보를 잘못 입력하셨습니다.");
            System.out.println("로그인 실패. 입력된 ID: " + memberId);
            return "redirect:/member/loginpage";
        }
    }
    // 회원가입 폼
    @GetMapping("member/joinmember")
    public String showJoinForm(Model model) {
        model.addAttribute("memberVO", new MemberVO());
        return "member/joinmember";
    }
    // 회원가입 기능
    @PostMapping("member/joinmember")
    public String processJoinForm(@Valid MemberVO memberVO, BindingResult result) {
        if (result.hasErrors()) {
            return "member/joinmember";
        }

        if (mService.isIdDuplicate(memberVO.getMemberId())) {
            result.rejectValue("memberId", "error.memberId", "이미 사용 중인 아이디입니다.");
            return "member/joinmember";
        }

        if (mService.isEmailDuplicate(memberVO.getMemberEmail())) {
            result.rejectValue("memberEmail", "error.memberEmail", "이미 사용 중인 이메일입니다.");
            return "member/joinmember";
        }

        if (mService.isNicknameDuplicate(memberVO.getMemberNickName())) {
            result.rejectValue("memberNickName", "error.memberNickName", "이미 사용 중인 닉네임입니다.");
            return "member/joinmember";
        }
        mService.registerMember(memberVO);
        return "redirect:/";
    }
    // 회원가입 중복체크
    @GetMapping("member/checkDuplicate")
    @ResponseBody
    public ResponseEntity<Map<String, Boolean>> checkDuplicate(@RequestParam String field, @RequestParam String value) {
        boolean isAvailable = false;
        switch(field) {
            case "id":
                isAvailable = !mService.isIdDuplicate(value);
                break;
            case "nickname":
                isAvailable = !mService.isNicknameDuplicate(value);
                break;
            case "email":
                isAvailable = !mService.isEmailDuplicate(value);
                break;
        }
        Map<String, Boolean> response = new HashMap<>();
        response.put("available", isAvailable);
        return ResponseEntity.ok(response);
    }
    // 로그아웃
    @GetMapping("member/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
    // 메인페이지로 리다이렉트
    @GetMapping("/")
    public String mainPage(Model model, HttpSession session) {
        MemberVO member = (MemberVO) session.getAttribute("member");
        if (member != null) {
            model.addAttribute("loggedIn", true);
            model.addAttribute("memberNickName", member.getMemberNickName());
        } else {
            model.addAttribute("loggedIn", false);
        }
        return "index";
    }


    }



