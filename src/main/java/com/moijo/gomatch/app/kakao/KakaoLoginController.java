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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/kakao")
public class KakaoLoginController {

    private final KakaoService kakaoService;

//    @GetMapping("/login")
//    public ResponseEntity<Map<String, String>> getKakaoLoginPage() {
//        String kakaoLoginUrl = kakaoService.getKakaoLoginUrl();
//        Map<String, String> response = new HashMap<>();
//        response.put("loginUrl", kakaoLoginUrl);
//        return ResponseEntity.ok(response);
//    }
    @GetMapping("/login")
    public String loginPage(Model model) {
    String kakaoLoginUrl = kakaoService.getKakaoLoginUrl();
    model.addAttribute("kakaoLoginUrl", kakaoLoginUrl);
    return "member/loginpage";  // Thymeleaf 템플릿 이름
}

    // redirect URI code 가져오기
    @GetMapping("/callback")
    public ResponseEntity<?> callback(@RequestParam("code") String code) throws IOException {
        KakaoLoginDto loginResult = kakaoService.processKakaoLogin(code);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/index"));
        headers.add("Set-Cookie", "access_token=" + loginResult.getAccessToken() + "; HttpOnly; Secure; SameSite=Strict");
        headers.add("Set-Cookie", "refresh_token=" + loginResult.getRefreshToken() + "; HttpOnly; Secure; SameSite=Strict");

        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }


}
