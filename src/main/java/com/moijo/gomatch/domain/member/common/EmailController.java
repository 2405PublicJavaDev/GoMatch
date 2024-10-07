package com.moijo.gomatch.domain.member.common;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @ResponseBody
    @PostMapping("/mail")
    public ResponseEntity<String> sendMail(@RequestParam String mail) {
        try {
            String number = emailService.sendMail(mail);
            return ResponseEntity.ok(String.valueOf(number));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("메일 전송 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    @ResponseBody
    @PostMapping("/member/verifyCode")
    public ResponseEntity<Map<String, Boolean>> verifyCode(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        String inputCode = payload.get("code");

        boolean isVerified = emailService.verifyCode(email, inputCode);

        Map<String, Boolean> response = new HashMap<>();
        response.put("verified", isVerified);

        return ResponseEntity.ok(response);
    }
}