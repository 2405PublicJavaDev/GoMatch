package com.moijo.gomatch.domain.member.service.impl;

import com.moijo.gomatch.domain.member.mapper.MemberMapper;
import com.moijo.gomatch.domain.member.service.MemberService;
import com.moijo.gomatch.domain.member.vo.MemberVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Member;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

   private final MemberMapper mapper;

    @Override
    public MemberVO checkLogin(MemberVO member) {
        MemberVO result = mapper.checkLogin(member);
        return result;
    }
}
