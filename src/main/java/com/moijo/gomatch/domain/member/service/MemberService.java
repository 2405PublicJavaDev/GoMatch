package com.moijo.gomatch.domain.member.service;

import com.moijo.gomatch.domain.member.vo.MemberVO;
import org.springframework.stereotype.Service;


public interface MemberService {

/**
 * 로그인 serivce
 * @author yoojung
 */
     MemberVO checkLogin(MemberVO member);

}
