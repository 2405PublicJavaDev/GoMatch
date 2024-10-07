package com.moijo.gomatch.domain.member.service.impl;

import com.moijo.gomatch.domain.member.mapper.MemberMapper;
import com.moijo.gomatch.domain.member.service.MemberService;
import com.moijo.gomatch.domain.member.vo.MemberVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Member;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

   private final MemberMapper mapper;


    @Override
    public MemberVO checkLogin(MemberVO member) {
        MemberVO result = mapper.checkLogin(member);
        if (result != null && result.getMemberPw().equals(member.getMemberPw())) {
            return result;
        }
        return null;
    }

    @Override
    public void registerMember(MemberVO memberVO) {
        // 디폴트값
        memberVO.setKakaoLoginYn("N");
        memberVO.setMatchPredictExp(0);
        memberVO.setMemberStatus("NORMAL");
        memberVO.setRegDate(Timestamp.valueOf(LocalDateTime.now()));
        memberVO.setUpdateDate(Timestamp.valueOf(LocalDateTime.now()));

        mapper.insertMember(memberVO);
    }

    @Override
    public boolean isIdDuplicate(String memberId) {
        return mapper.countByMemberId(memberId) > 0;
    }

    @Override
    public boolean isEmailDuplicate(String memberEmail) {
        return mapper.countByMemberEmail(memberEmail) > 0;
    }

    @Override
    public boolean isNicknameDuplicate(String memberNickname) {
        return mapper.countByMemberNickname(memberNickname) > 0;
    }


}
