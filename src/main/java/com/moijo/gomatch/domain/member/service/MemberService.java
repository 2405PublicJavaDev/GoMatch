package com.moijo.gomatch.domain.member.service;

import com.moijo.gomatch.domain.member.vo.MemberVO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MemberService {

    // 사용자 로그인을 확인
    MemberVO checkLogin(MemberVO member);

    // 새로운 회원 등록
    void registerMember(MemberVO memberVO);

    // 아이디 중복 검사
    boolean isIdDuplicate(String memberId);

    // 이메일 중복 검사
    boolean isEmailDuplicate(String memberEmail);

    // 닉네임 중복 검사
    boolean isNicknameDuplicate(String memberNickname);

    // 이름과 생년월일로 아이디 찾기
    String findIdByNameAndBirthDate(String name, String birthDate);

    // 임시 비밀번호 방금하여 비밀번호 재설정
    boolean resetPassword(String memberId, String email);

    // 사용자 비밀번호 확인
    boolean checkPassword(String memberId, String password);

    // 프로필 이미지 업로드
    String uploadProfileImage(MultipartFile file);

    // 특정 아이디 회원 정보 조회
    MemberVO getMemberById(String memberId);

    // 회원 정보수정
    boolean modifyMember(MemberVO memberVO);

    // 회원 탈퇴 처리
    boolean deleteMember(String memberId, String password);

    // 이메일로 회원 정보 조회
    MemberVO getMemberByEmail(String email);


}
