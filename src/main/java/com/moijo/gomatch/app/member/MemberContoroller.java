package com.moijo.gomatch.app.member;

import com.moijo.gomatch.app.game.GameBatchComponent;
import com.moijo.gomatch.domain.game.service.GameService;
import com.moijo.gomatch.domain.kakao.service.KakaoService;
import com.moijo.gomatch.domain.member.service.ImageService;
import com.moijo.gomatch.domain.member.service.MemberService;
import com.moijo.gomatch.domain.member.vo.MemberVO;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberContoroller {

    @Autowired
    private KakaoService kakaoService;
    private final MemberService mService;
    private final ImageService imageService;
    @Value("${kakao.client_id}")
    private String clientId;

    @Value("${kakao.redirect_uri}")
    private String redirectUri;

    @Autowired
    private GameBatchComponent gameBatchComponent;


    /**
     * 담당자: 이용재
     * 관련 기능: 로그인 페이지 표시
     * 설명: 로그인 페이지 표시, 카카오 로그인 URL을 모델로 추가
     */
    @GetMapping("/member/loginpage")
    public String showLoginPage(Model model) {
        String kakaoLoginUrl = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id="
                + clientId + "&redirect_uri=" + redirectUri;
        model.addAttribute("kakaoLoginUrl", kakaoLoginUrl);
        return "member/loginpage";
    }

    /**
     * 담당자: 이용재
     * 관련 기능: 사용자 로그인 기능
     * 설명: 로그인 성공시 세션 저장, 관리자/일반 사용자 다른페이지 리다이렉트함
     */
    @PostMapping("/member/loginpage")
    @ResponseBody
    public ResponseEntity<?> loginpage(@RequestParam("memberId") String memberId,
                                       @RequestParam("memberPw") String memberPw,
                                       HttpSession session) {
        MemberVO member = new MemberVO();
        member.setMemberId(memberId);
        member.setMemberPw(memberPw);

        member = mService.checkLogin(member);
        if (member != null) {
            // 세션에 사용자 정보 저장
            session.setAttribute("member", member);
            session.setAttribute("memberId", member.getMemberId());
            session.setAttribute("memberNickName", member.getMemberNickName());
            session.setAttribute("preferenceClub", member.getPreferenceClub());
            session.setAttribute("matchPredictExp", member.getMatchPredictExp());
            session.setAttribute("loggedIn", true);

            System.out.println("로그인 성공: " + member.getMemberNickName());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);

            // 관리자 계정 확인 및 리다이렉트 URL 설정
            if (member.getMemberId().startsWith("admin")) {
                response.put("redirectUrl", "/admin/admin-mainpage");
            } else {
                response.put("redirectUrl", "/");
            }

            return ResponseEntity.ok(response);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "회원 정보를 잘못 입력하셨습니다.");
            return ResponseEntity.ok(response);
        }
    }

    /**
     * 담당자: 이용재
     * 관련 기능: 회원가입 페이지 표시
     * 설명: 회원가입 페이지 표시
     */
    @GetMapping("member/joinmember")
    public String showJoinForm(Model model) {
        model.addAttribute("memberVO", new MemberVO());
        return "member/joinmember";
    }

    /**
     * 담당자: 이용재
     * 관련 기능: 회원가입 기능
     * 설명: 회원가입 처리를 하고 데이터 유효성 검사, 중복체크를 수행
     */
    @PostMapping("member/joinmember")
    public String processJoinForm(@Valid MemberVO memberVO, BindingResult result) {
        if (result.hasErrors()) {
            return "member/joinmember";
        }
        // 아이디 중복체크
        if (mService.isIdDuplicate(memberVO.getMemberId())) {
            result.rejectValue("memberId", "error.memberId", "이미 사용 중인 아이디입니다.");
            return "member/joinmember";
        }
        // 이메일 중복체크
        if (mService.isEmailDuplicate(memberVO.getMemberEmail())) {
            result.rejectValue("memberEmail", "error.memberEmail", "이미 사용 중인 이메일입니다.");
            return "member/joinmember";
        }
        // 닉네임 중복체크
        if (mService.isNicknameDuplicate(memberVO.getMemberNickName())) {
            result.rejectValue("memberNickName", "error.memberNickName", "이미 사용 중인 닉네임입니다.");
            return "member/joinmember";
        }
        mService.registerMember(memberVO);
        return "redirect:/";
    }

    /**
     * 담당자: 이용재
     * 관련 기능: 중복 여부 확인
     * 설명: 아이디, 닉네임, 이메일의 중복 여부 확인
     */
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

    /**
     * 담당자: 이용재
     * 관련 기능: 로그아웃 기능 처리
     * 설명: 로그아웃 처리 후 세션 무효화
     */
    @GetMapping("/member/logout")
    public String logout(HttpSession session) {
        // 세션 속성 제거
        session.removeAttribute("loggedIn");
        session.removeAttribute("memberNickName");
        session.removeAttribute("kakaoAccessToken");
        session.removeAttribute("kakaoRefreshToken");
        session.removeAttribute("jwtToken");
        // 세션 무효화
        session.invalidate();
        return "redirect:/";
    }

    // ========================================================= 헤더 세션 ========================================================= //
    /**
     * 담당자: 이용재
     * 관련 기능: 메인 페이지 표시
     * 설명: 로그인한 상태에 따라 모델 정보를 추가함 (로그인 했을때 아닐때)
     */
    @GetMapping("/")
    public String mainPage(Model model, HttpSession session) {
        boolean loggedIn = session.getAttribute("loggedIn") != null && (Boolean) session.getAttribute("loggedIn");
        model.addAttribute("loggedIn", loggedIn);
        if (loggedIn) {
            model.addAttribute("memberNickName", session.getAttribute("memberNickName"));
        }
        List<String[]> teamList_2024 = gameBatchComponent.scrapeTeamRank("2024"); // 2024년도 팀 순위 
        session.setAttribute("2024_Rank", teamList_2024); // 세션에 저장
        return "index";
    }
    /**
     * 담당자: 이용재
     * 관련 기능: 닉네임 세션 추가
     * 설명: 모든 요청에 로긍니 상태와 닉네임 모델을 추가 함
     */
    @ModelAttribute
    public void addAttributes(Model model, HttpSession session) {
        MemberVO member = (MemberVO) session.getAttribute("member");
        if (member != null) {
            model.addAttribute("loggedIn", true);
            model.addAttribute("memberNickName", member.getMemberNickName());
        } else {
            model.addAttribute("loggedIn", false);
        }
    }
    // =========================================================================================================================== //

    /**
     * 담당자: 이용재
     * 관련 기능: 아이디 찾기 페이지 표시
     * 설명: 아이디 찾기 페이지 표시
     */
    @GetMapping("/member/findid")
    public String showFindIdForm() {
        return "member/findid";
    }

    /**
     * 담당자: 이용재
     * 관련 기능: 아이디 찾기 기능
     * 설명: 이름과 생년월일을 이용해 아이디를 찾기
     */
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

    /**
     * 담당자: 이용재
     * 관련 기능: 아이디를 마스킹(lee****) 하는 역할
     * 설명: 컨트롤러 내부에 사용 되는 헬퍼 메서드 현재 단순히 아이디만 반환 중
     *       마스킹 하려면 코드 추가
     */
    private String maskId(String id) {
        return id;
    }

    /**
     * 담당자: 이용재
     * 관련 기능: 비밀번호 찾기 페이지 표시
     * 설명: 비밀번호 찾기 페이지 표시
     */
    @GetMapping("/member/findpw")
    public String showFindPwdForm() {
        return "member/findpw";
    }

    /**
     * 담당자: 이용재
     * 관련 기능: 비밀번호 찾기(임시 비밀번호 전송) 기능
     * 설명: 비밀번호 찾기 시 아이디, 이메일로 찾고 입력된 이메일로 임시 비밀번호 전송
     *       데이터베이스에 임시비밀번호로 변경됨
     */
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

    /**
     * 담당자: 이용재
     * 관련 기능: 마이페이지 표시
     * 설명: 마이페이지 표시, 일반/카카오 구분해서 정보 처리함
     */
    @GetMapping("/member/mypage")
    public String showMyPageForm(HttpSession session, Model model) {
        // 로그인 체크
        Boolean loggedIn = (Boolean) session.getAttribute("loggedIn");
        if (loggedIn == null || !loggedIn) {
            return "redirect:/member/loginpage";
        }

        String memberNickName = (String) session.getAttribute("memberNickName");
        MemberVO member;

        // 카카오 로그인 사용자 처리
        if (session.getAttribute("kakaoAccessToken") != null) {
            // 카카오 사용자 정보로 MemberVO 생성 또는 조회
            member = createOrGetKakaoMember(session);
        } else {
            // 일반 로그인 사용자
            member = (MemberVO) session.getAttribute("member");
        }

        if (member == null) {
            return "redirect:/member/loginpage";
        }

        model.addAttribute("memberVO", member);
        model.addAttribute("memberName", member.getMemberName());
        model.addAttribute("memberNickName", member.getMemberNickName());

        // 프로필 이미지 URL 처리
        String profileImageUrl = member.getProfileImageUrl();
        if (profileImageUrl == null || profileImageUrl.isEmpty()) {
            profileImageUrl = "/img/default-profile.png"; // 기본 이미지 경로
        }
        model.addAttribute("profileImageUrl", profileImageUrl);

        return "member/mypage";
    }

    /**
     * 담당자: 이용재
     * 관련 기능: 카카오 로그인 사용자 정보 처리
     * 설명: 헬퍼 메서드 이메일을 사용해서 기존 회원인지 확인
     *       세션에서 카카오 닉네임 이메일 가져옴
     *       기존 회원이 아니면 MemberVO에 객체를 생성하고 카카오 정보로 초기화함
     *       프로필 이미지가 있다면 URL 회원정보에 설정됨
     */
    private MemberVO createOrGetKakaoMember(HttpSession session) {
        String kakaoNickname = (String) session.getAttribute("memberNickName");
        String kakaoEmail = (String) session.getAttribute("kakaoEmail"); // 세션에 저장된 카카오 이메일

        // 이메일로 회원 조회
        MemberVO member = mService.getMemberByEmail(kakaoEmail);

        if (member == null) {
            // 새 회원 생성
            member = new MemberVO();
            member.setMemberNickName(kakaoNickname);
            member.setMemberEmail(kakaoEmail);
            member.setKakaoLoginYn("Y");
            // 기타 필요한 정보 설정

            // 회원 저장 (실제 구현 필요)
            // memberService.registerMember(member);
        }

        // 카카오 프로필 이미지 URL 설정 (있다면)
        String kakaoProfileImageUrl = (String) session.getAttribute("kakaoProfileImageUrl");
        if (kakaoProfileImageUrl != null && !kakaoProfileImageUrl.isEmpty()) {
            member.setProfileImageUrl(kakaoProfileImageUrl);
        }

        return member;
    }

    /**
     * 담당자: 이용재
     * 관련 기능: 현재 비밀번호 유효성 검사
     * 설명: 현재 페이지 유효성 검사
     */
    @PostMapping("/member/checkCurrentPassword")
    @ResponseBody
    public ResponseEntity<Map<String, Boolean>> checkCurrentPassword(@RequestParam String currentPassword,
                                                                     HttpSession session) {
        MemberVO currentMember = (MemberVO) session.getAttribute("member");
        Map<String, Boolean> response = new HashMap<>();
        if (currentMember != null) {
            boolean isValid = mService.checkPassword(currentMember.getMemberId(), currentPassword);
            response.put("valid", isValid);
            log.info("Password check for user {}: {}", currentMember.getMemberId(), isValid);
        } else {
            response.put("valid", false);
            log.warn("Attempt to check password without logged in user");
        }
        return ResponseEntity.ok(response);
    }

    /**
     * 담당자: 이용재
     * 관련 기능: 회원정보 수정 페이지 표시
     * 설명: 회원 정보 수정 페이지 표시
     */
    @GetMapping("/member/modifymember")
    public String showModifyMemberForm(Model model, HttpSession session) {
        MemberVO member = (MemberVO) session.getAttribute("member");
        if (member == null) {
            return "redirect:/member/loginpage";

        }
        model.addAttribute("memberVO", member);
        model.addAttribute("memberName", member.getMemberName());
        model.addAttribute("memberNickName", member.getMemberNickName());
        return "member/modifymember";
    }

    /**
     * 담당자: 이용재
     * 관련 기능: 회원정보 수정 처리 기능
     * 설명: 회원정보 수정 처리(비밀번호 변경, 프로필 이미지 업로드 포함)
     */
    @PostMapping("/member/modifymember")
    public ResponseEntity<?> modifyMember(@ModelAttribute @Valid MemberVO memberVO,
                                          BindingResult bindingResult,
                                          @RequestParam(required = false) MultipartFile profileImage,
                                          @RequestParam(required = false) String currentPassword,
                                          @RequestParam(required = false) String newPassword,
                                          HttpSession session) {
        try {
            MemberVO currentMember = (MemberVO) session.getAttribute("member");
            if (currentMember == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
            }

            if (bindingResult.hasErrors()) {
                return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
            }

            memberVO.setMemberId(currentMember.getMemberId());

            // 비밀번호 변경 처리
            if (StringUtils.hasText(currentPassword) && StringUtils.hasText(newPassword)) {
                log.info("Attempting to change password for user: {}", currentMember.getMemberId());
                if (!mService.checkPassword(currentMember.getMemberId(), currentPassword)) {
                    log.error("Current password mismatch for user: {}", currentMember.getMemberId());
                    return ResponseEntity.badRequest().body("현재 비밀번호가 일치하지 않습니다.");
                }
                memberVO.setMemberPw(newPassword);
                log.info("Password changed successfully for user: {}", currentMember.getMemberId());
            } else {
                log.info("No password change requested for user: {}", currentMember.getMemberId());
                memberVO.setMemberPw(currentMember.getMemberPw());
            }

            // 프로필 이미지 처리
            if (profileImage != null && !profileImage.isEmpty()) {
                String imageUrl = imageService.saveProfileImage(profileImage, currentMember.getProfileImageUrl());
                memberVO.setProfileImageUrl(imageUrl);
                log.info("Profile image updated for user: {}, URL: {}", currentMember.getMemberId(), imageUrl);
            } else {
                memberVO.setProfileImageUrl(currentMember.getProfileImageUrl());
            }

            boolean modified = mService.modifyMember(memberVO);
            if (modified) {
                session.setAttribute("member", mService.getMemberById(currentMember.getMemberId()));
                log.info("Member information updated successfully for user: {}", currentMember.getMemberId());
                return ResponseEntity.ok("회원정보가 성공적으로 수정되었습니다.");
            } else {
                log.error("Failed to update member information for user: {}", currentMember.getMemberId());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("회원정보 수정에 실패했습니다.");
            }

        } catch (Exception e) {
            log.error("Error occurred while modifying member", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("오류가 발생했습니다: " + e.getMessage());
        }
    }

    /**
     * 담당자: 이용재
     * 관련 기능: 회원탈퇴 페이지 표시
     * 설명: 회원탈퇴 페이지 표시
     */
    @GetMapping("/member/deletemember")
    public String deleteMemberForm(HttpSession session, Model model) {
        log.info("Accessing deleteMemberForm");
        MemberVO member = (MemberVO) session.getAttribute("member");
        if (member == null) {
            log.warn("Attempt to access deleteMemberForm without login");
            return "redirect:/member/loginpage";
        }
        model.addAttribute("memberVO", member);
        log.info("DeleteMemberForm loaded successfully for user: {}", member.getMemberId());
        return "member/deletemember";
    }

    /**
     * 담당자: 이용재
     * 관련 기능: 회원탈퇴 기능
     * 설명: 회원 탈퇴 처리, 비밀번호 확인후 계정 삭제 디비에서도 삭제 됨
     */
    @PostMapping("/member/delete")
    public ResponseEntity<?> deleteMember(@RequestParam String password, HttpSession session) {
        MemberVO member = (MemberVO) session.getAttribute("member");
        if (member == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        try {
            boolean deleted = mService.deleteMember(member.getMemberId(), password);
            if (deleted) {
                session.invalidate();
                return ResponseEntity.ok("회원 탈퇴가 완료되었습니다.");
            } else {
                return ResponseEntity.badRequest().body("비밀번호가 일치하지 않습니다.");
            }
        } catch (DataIntegrityViolationException e) {
            log.error("회원 탈퇴 중 데이터 무결성 오류 발생", e);
            return ResponseEntity.status(HttpStatus.CONFLICT).body("회원과 관련된 데이터로 인해 탈퇴할 수 없습니다. 관리자에게 문의해주세요.");
        } catch (Exception e) {
            log.error("회원 탈퇴 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("회원 탈퇴 처리 중 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");
        }
    }
    /**
     * 담당자: 이용재
     * 관련 기능: 카카오 연동 해제
     * 설명: 카카오 로그인 계정 연동 해제
     */
    @PostMapping("/member/unlink-kakao")
    @ResponseBody
    public Map<String, Boolean> unlinkKakao(HttpSession session) {
        Map<String, Boolean> response = new HashMap<>();
        String kakaoAccessToken = (String) session.getAttribute("kakaoAccessToken");
        MemberVO member = (MemberVO) session.getAttribute("member");

        if (kakaoAccessToken == null || member == null) {
            response.put("success", false);
            return response;
        }

        boolean unlinked = kakaoService.unlinkKakaoAccount(kakaoAccessToken);
        if (unlinked) {
            member.setKakaoLoginYn("N");
            mService.modifyMember(member);
            session.removeAttribute("kakaoAccessToken");
        }

        response.put("success", unlinked);
        return response;
    }


}



