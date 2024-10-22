package com.moijo.gomatch.domain.member.mapper;

import com.moijo.gomatch.domain.member.vo.MemberVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Mapper
public interface MemberMapper {

    // 사용자 로그인을 확인
    MemberVO checkLogin(MemberVO member);

    // 새로운 회원 등록
    void insertMember(MemberVO memberVO);

    // 아이디 중복체크
    int countByMemberId(String memberId);

    // 이메일 중복체크
    int countByMemberEmail(String memberEmail);

    // 닉네임 중복체크
    int countByMemberNickname(String memberNickname);

    // 아이디 찾기 (이름,생년월일)
    String findIdByNameAndBirthDate(@RequestParam ("name") String name, @RequestParam("birthDate") String birthDate);

    // 비밀번호 찾기 (임시비밀번호 전송)
    MemberVO findByIdAndEmail(@RequestParam("memberId") String memberId, @RequestParam("email") String email);

    // 임시비밀번호 디비 저장
    void updatePassword(@RequestParam("memberId") String memberId, @RequestParam("password") String password);

    // 회원정보 수정
    int updateMember(MemberVO memberVO);

    // 특정 아이디 조회
    MemberVO getMemberById(String memberId);

    // 회원 정보 수정
    int modifyMember(MemberVO memberVO);

    // 회원 탈퇴 처리
    void deleteMember(String memberId);

    // 연관된 랭크 정보 삭제
    void deleteMemberRank(String memberId);

    // 이메일로 회원 정보 조회
    MemberVO getMemberByEmail(String email);

}

