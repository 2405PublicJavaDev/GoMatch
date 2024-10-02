package com.moijo.gomatch.domain.member.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.Date;

@Getter
@Setter
@ToString
public class MemberVO {



    private String memberId;
    private String memberPw;
    private String memberNickName;
    private String memberEmail;
    private String memberName;
    private Date birthDate;
    private String kakaoLoginYn;
    private int matchPredictExp;
    private String memberStatus;
    private Timestamp regDate;
    private Timestamp updateDate;
    private String preferenceClub;
    private String kakaoProfile;
    private MemberFile memberFile;




}
