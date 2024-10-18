//package com.moijo.gomatch.app.kakao;
//
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//@Controller
//public class KaKaoLoginPageController {
//
//    @Value("${kakao.client_id}")
//    private String clientId;
//
//    @Value("${kakao.redirect_uri}")
//    private String redirectUri;
//
//    @GetMapping("/member/loginpage")
//    public String loginPage(Model model) {
//        String kakaoLoginUrl = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id="
//                + clientId + "&redirect_uri=" + redirectUri;
//        model.addAttribute("kakaoLoginUrl", kakaoLoginUrl);
//        return "member/loginpage";
//    }
//
//
//}
