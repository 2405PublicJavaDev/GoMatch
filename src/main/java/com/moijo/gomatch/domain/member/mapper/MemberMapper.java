package com.moijo.gomatch.domain.member.mapper;

import com.moijo.gomatch.domain.member.vo.MemberVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Mapper
public interface MemberMapper {


    /**
     * 로그인
     * @param member
     * @return
     */
    MemberVO checkLogin(MemberVO member);


    /**
     * 회원가입 mapper
     * @param memberVO
     */
    void insertMember(MemberVO memberVO);

    /**
     *  아이디 중복체크
     * @param memberId
     * @return
     */
    int countByMemberId(String memberId);

    /**
     * 이메일 중복체크
     * @param memberEmail
     * @return
     */
    int countByMemberEmail(String memberEmail);

    /**
     * 닉네임 중복체크
     * @param memberNickname
     * @return
     */
    int countByMemberNickname(String memberNickname);

    /**
     * 아이디 찾기(이름,닉네임)
     * @param name
     * @param birthDate
     * @return
     */
    String findIdByNameAndBirthDate(@RequestParam ("name") String name, @RequestParam("birthDate") String birthDate);

    /**
     * 비밀번호 재설정 할 회원 아이디,이메일로 조회
     * @param memberId
     * @param email
     * @return
     */
    MemberVO findByIdAndEmail(@RequestParam("memberId") String memberId, @RequestParam("email") String email);

    /**
     * 회원 비밀번호 DB업데이트
     * @param memberId
     * @param password
     */
    void updatePassword(@RequestParam("memberId") String memberId, @RequestParam("password") String password);


    /**
     * 회원정보 수정
     * @param memberVO
     * @return
     */
    int updateMember(MemberVO memberVO);

    /**
     * 특정 아이디 조회
     * @param memberId
     * @return
     */
    MemberVO getMemberById(String memberId);

    /**
     * 회원 정보 수정
     * @param memberVO
     * @return
     */
    int modifyMember(MemberVO memberVO);

    /**
     * 회원 탈퇴
     * @param memberId
     */
    void deleteMember(String memberId);

    /**
     * 탈퇴시 랭크DB 정보도 삭제
     * @param memberId
     */
    void deleteMemberRank(String memberId);


    MemberVO getMemberByEmail(String email);

}

