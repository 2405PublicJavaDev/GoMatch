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

    // 인증번호 난수 생성기(6자리)
    public static void createNumber(String email) {
        String number = String.format("%06d", (int) (Math.random() * 1000000));
        verificationCodes.put(email, number);
    }

    // 인증된 이메일에게 인증번호 담기
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

    //인증번호 포함된 이메일 전송
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

    // 인증번호 올바른지 검증
    public boolean verifyCode(String email, String inputCode) {
        String storedCode = verificationCodes.get(email);
        if (storedCode != null && storedCode.equals(inputCode)) {
            verificationCodes.remove(email);  // 사용된 코드는 삭제
            return true;
        }
        return false;
    }

    // 임시비밀번호 생성하여 이메일로 전송
    public void sendTempPassword(String mail, String tempPassword) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            message.setFrom(senderEmail);
            message.setRecipients(MimeMessage.RecipientType.TO, mail);
            message.setSubject("임시 비밀번호 발급");
            String body = "";
            body += "<h3>임시 비밀번호가 발급되었습니다.</h3>";
            body += "<h1>" + tempPassword + "</h1>";
            body += "<h3>보안을 위해 로그인 후 비밀번호를 변경해주세요.</h3>";
            message.setText(body, "UTF-8", "html");
            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("임시 비밀번호 메일 전송 중 오류가 발생했습니다.", e);
        }
    }

}
