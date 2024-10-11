package com.moijo.gomatch.domain.member.vo;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@ToString
public class MemberVO {

    @NotBlank(message = "아이디는 필수 입력 항목입니다.")
    @Size(min = 5, max = 20, message = "아이디는 5자 이상 20자 이하여야 합니다.")
    private String memberId;

    @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
    @Size(min = 8, message = "비밀번호는 8자 이상이어야 합니다.")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}",
            message = "비밀번호는 영문, 숫자, 특수문자를 포함해야 합니다.")
    private String memberPw;

    @NotBlank(message = "닉네임은 필수 입력 항목입니다.")
    @Size(min = 2, max = 20, message = "닉네임은 2자 이상 20자 이하여야 합니다.")
    private String memberNickName;

    @NotBlank(message = "이메일은 필수 입력 항목입니다.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String memberEmail;

    @NotBlank(message = "이름은 필수 입력 항목입니다.")
    private String memberName;

    @NotNull(message = "생년월일은 필수 입력 항목입니다.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    private String kakaoLoginYn;
    private int matchPredictExp;
    private String memberStatus;
    private Timestamp regDate;
    private Timestamp updateDate;
    private String preferenceClub;
    private String kakaoProfile;
    private String profileImageUrl;

//    private boolean emailVerified = false;
//    private String emailVerificationCode;


}
