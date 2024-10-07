package com.moijo.gomatch.domain.member.service;

import com.moijo.gomatch.domain.member.vo.MemberVO;
import org.springframework.stereotype.Service;

public interface MemberService {

    /**
     * 로그인 serivce
     * @author yoojung
     */
     MemberVO checkLogin(MemberVO member);

    /**
     * 회원가입 service
     *
     */

    void registerMember(MemberVO memberVO);
    boolean isIdDuplicate(String memberId);
    boolean isEmailDuplicate(String memberEmail);
    boolean isNicknameDuplicate(String memberNickname);
}
