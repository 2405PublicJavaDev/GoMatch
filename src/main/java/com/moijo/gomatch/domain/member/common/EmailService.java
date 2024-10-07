package com.moijo.gomatch.domain.member.common;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private static final String senderEmail = "jjinmoijo5@gmail.com";
    private static Map<String, String> verificationCodes = new HashMap<>();

    public static void createNumber(String email) {
        String number = String.format("%06d", (int) (Math.random() * 1000000));
        verificationCodes.put(email, number);
    }

    public MimeMessage CreateMail(String mail) {
        createNumber(mail);
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            message.setFrom(senderEmail);
            message.setRecipients(MimeMessage.RecipientType.TO, mail);
            message.setSubject("이메일 인증");
            String body = "";
            body += "<h3>" + "요청하신 인증 번호입니다." + "</h3>";
            body += "<h1>" + verificationCodes.get(mail) + "</h1>";
            body += "<h3>" + "감사합니다." + "</h3>";
            message.setText(body, "UTF-8", "html");
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException("메일 생성 중 오류가 발생했습니다.", e);
        }

        return message;
    }

    public String sendMail(String mail) {
        MimeMessage message = CreateMail(mail);
        try {
            javaMailSender.send(message);
            return verificationCodes.get(mail);
        } catch (MailException e) {
            e.printStackTrace();
            throw new RuntimeException("메일 전송 중 오류가 발생했습니다.", e);
        }
    }

    public boolean verifyCode(String email, String inputCode) {
        String storedCode = verificationCodes.get(email);
        if (storedCode != null && storedCode.equals(inputCode)) {
            verificationCodes.remove(email);  // 사용된 코드는 삭제
            return true;
        }
        return false;
    }
}
