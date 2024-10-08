package com.moijo.gomatch.app.member;

import com.moijo.gomatch.domain.member.service.MemberService;
import com.moijo.gomatch.domain.member.vo.MemberVO;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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
            session.setAttribute("preferenceClub", member.getPreferenceClub());
            session.setAttribute("matchPredictExp", member.getMatchPredictExp());
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
    @GetMapping("/member/findid")
    public String showFindIdForm() {
        return "member/findid";
    }

    @PostMapping("/member/findid")
    public String findId(@RequestParam String name,
                         @RequestParam String birthDate,
                         Model model) {
        try {
            // 입력받은 생년월일 문자열을 LocalDate로 변환
            LocalDate parsedBirthDate = LocalDate.parse(birthDate, DateTimeFormatter.ofPattern("yyyyMMdd"));
            String formattedBirthDate = parsedBirthDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            String foundId = mService.findIdByNameAndBirthDate(name, formattedBirthDate);
            if (foundId != null) {
                String maskedId = maskId(foundId);
                model.addAttribute("foundId", maskedId);
            } else {
                model.addAttribute("errorMessage", "일치하는 회원 정보를 찾을 수 없습니다.");
            }
        } catch (DateTimeParseException e) {
            model.addAttribute("errorMessage", "올바른 생년월일 형식이 아닙니다. (예: 19901231)");
        } catch (Exception e) {
            model.addAttribute("errorMessage", "아이디 찾기 중 오류가 발생했습니다.");
            e.printStackTrace();
        }
        return "member/findid";
    }

    private String maskId(String id) {

        return id;
    }
// 비밀번호 찾기 폼
    @GetMapping("/member/findpw")
    public String showFindPwdForm() {
        return "member/findpw";
    }

// 비밀번호 찾기( 임시비밀번호 전송) 기능
    @PostMapping("/member/findpw")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> resetPassword(@RequestParam String memberId, @RequestParam String email) {
        Map<String, Object> response = new HashMap<>();
        boolean isReset = mService.resetPassword(memberId, email);
        if (isReset) {
            response.put("success", true);
            response.put("message", "임시 비밀번호가 이메일로 전송되었습니다.");
            System.out.println("임시 비밀번호 전송: " + memberId);
        } else {
            response.put("success", false);
            response.put("message", "일치하는 회원 정보를 찾을 수 없습니다.");
            System.out.println("실패실패실패: " + memberId);
        }
        return ResponseEntity.ok(response);
    }

    // 마이페이지 폼
    @GetMapping("/member/mypage")
    public String showMyPageForm(){
        return "member/mypage";
    }

    }



