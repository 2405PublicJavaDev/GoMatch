package com.moijo.gomatch.domain.member.mapper;

import com.moijo.gomatch.domain.member.vo.MemberVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper {

    /**
     * 로그인 mapper
     */
    MemberVO checkLogin(MemberVO member);


    /**
     * 회원가입 mapper
     * @param memberVO
     */
    void insertMember(MemberVO memberVO);
    int countByMemberId(String memberId);
    int countByMemberEmail(String memberEmail);
    int countByMemberNickname(String memberNickname);

}
