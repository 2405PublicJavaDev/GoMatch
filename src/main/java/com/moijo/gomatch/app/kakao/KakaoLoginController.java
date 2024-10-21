package com.moijo.gomatch.app.kakao;

import com.moijo.gomatch.domain.kakao.dto.KakaoLoginDto;
import com.moijo.gomatch.domain.kakao.dto.KakaoUserInfoResponseDto;
import com.moijo.gomatch.domain.kakao.service.KakaoService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("")
public class KakaoLoginController {

    private final KakaoService kakaoService;

    /**
     * 담당자: 이용재
     * 관련 기능: 카카오 로그인 콜백 처리 담당
     * 설명: 로그인 성공시 필요한정보 세선에 저장
     *       RequestParam("code")은 카카오 인증 서버에서 반환된 인증 코드
     *       jwt토큰 사용해서 카카오 로그인 구현 했음
     */
    @GetMapping("/callback")
    public String callback(@RequestParam("code") String code, HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            // kakaoService에 code를 호출하여 로그인 처리를 수행
            KakaoLoginDto loginResult = kakaoService.processKakaoLogin(code);
            // 로그인 성공시 세션 저장
            session.setAttribute("loggedIn", true);
            session.setAttribute("memberNickName", loginResult.getNickname());
            session.setAttribute("kakaoAccessToken", loginResult.getAccessToken());
            session.setAttribute("kakaoRefreshToken", loginResult.getRefreshToken());
            session.setAttribute("jwtToken", loginResult.getAccessToken());

            return "redirect:/";
        } catch (Exception e) {
            log.error("Error during Kakao login process", e);
            redirectAttributes.addFlashAttribute("error", "카카오 로그인 처리 중 오류가 발생했습니다.");
            return "redirect:/member/loginpage";
        }
    }
}
