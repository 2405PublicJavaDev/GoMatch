package com.moijo.gomatch.domain.member.mapper;

import com.moijo.gomatch.domain.member.vo.MemberVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper {

    /**
     * 로그인 mapper
     *
     */
    MemberVO checkLogin(MemberVO member);
}
