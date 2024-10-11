package com.moijo.gomatch.domain.member.mapper;

import com.moijo.gomatch.domain.member.vo.MemberVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.web.bind.annotation.RequestParam;

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

    /**
     * 아이디 찾기 mapper
     */
    String findIdByNameAndBirthDate(@RequestParam ("name") String name, @RequestParam("birthDate") String birthDate);

    /**
     * 비밀번호 찾기(임시비밀번호 발급) mapper
     */
    MemberVO findByIdAndEmail(@RequestParam("memberId") String memberId, @RequestParam("email") String email);
    void updatePassword(@RequestParam("memberId") String memberId, @RequestParam("password") String password);

    /**
     * 회원정보 수정 mapper
     */
    int updateMember(MemberVO memberVO);
    MemberVO getMemberById(String memberId);

    int modifyMember(MemberVO memberVO);

    void deleteMember(String memberId);
}
