package com.moijo.gomatch.common;

import java.security.SecureRandom;

public class VerificationCodeGenerator {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int CODE_LENGTH = 8;  // 6~8자리로 충분함
    private static final SecureRandom random = new SecureRandom();

    public static String generateVerificationCode() {
        StringBuilder sb = new StringBuilder(CODE_LENGTH);

        // 지정된 길이만큼 랜덤한 문자열 생성
        for (int i = 0; i < CODE_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }

        return sb.toString();
    }

    public static void main(String[] args) {
        // 이메일 인증 코드 생성
        String verificationCode = generateVerificationCode();
        System.out.println("Generated Verification Code: " + verificationCode);
    }
}