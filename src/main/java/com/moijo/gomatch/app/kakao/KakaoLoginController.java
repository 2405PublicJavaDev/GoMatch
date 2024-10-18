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

//    @GetMapping("/callback")
//    public ResponseEntity<?> callback(@RequestParam("code") String code, HttpSession session) {
//        try {
//            KakaoLoginDto loginResult = kakaoService.processKakaoLogin(code);
//            session.setAttribute("kakaoLoginSuccess", true);
//            session.setAttribute("kakaoAccessToken", loginResult.getAccessToken());
//            session.setAttribute("kakaoRefreshToken", loginResult.getRefreshToken());
//
//            Map<String, Object> response = new HashMap<>();
//            response.put("success", true);
//            response.put("message", "카카오 로그인 성공");
//            response.put("redirectUrl", "/");
//
//            log.info("Kakao login successful. Returning success response.");
//            return ResponseEntity.ok(response);
//        } catch (Exception e) {
//            log.error("Error during Kakao login process", e);
//            Map<String, Object> response = new HashMap<>();
//            response.put("success", false);
//            response.put("message", "카카오 로그인 처리 중 오류가 발생했습니다.");
//            return ResponseEntity.badRequest().body(response);
//        }
//    }

    @GetMapping("/callback")
    public String callback(@RequestParam("code") String code, HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            KakaoLoginDto loginResult = kakaoService.processKakaoLogin(code);
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
