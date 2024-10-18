package com.moijo.gomatch.domain.member.service;

import com.moijo.gomatch.domain.member.vo.MemberVO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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

    /**
     * 아이디 중복검사
     * @param memberId
     * @return
     */
    boolean isIdDuplicate(String memberId);

    /**
     * 이메일 중복검사
     * @param memberEmail
     * @return
     */
    boolean isEmailDuplicate(String memberEmail);

    /**
     * 닉네임 중복검사
     * @param memberNickname
     * @return
     */
    boolean isNicknameDuplicate(String memberNickname);

    /**
     * 아이디 찾기(이름, 생년월일) service
     * @param name
     * @param birthDate
     * @return
     */
    String findIdByNameAndBirthDate(String name, String birthDate);

    /**
     * 비밀번호 찾기(임시 비밀번호 발급) service
     * @param memberId
     * @param email
     */
    boolean resetPassword(String memberId, String email);

    /**
     * 비밀번호 인증 Service
     * @param memberId
     * @param password
     * @return
     */

    boolean checkPassword(String memberId, String password);
    /**
     * 프로필 이미지 업로드 service
     * @param file
     * @return
     */
    String uploadProfileImage(MultipartFile file);
    /**
     * 회원 정보 조회(특정아이디 정보) service
     * @param memberId
     * @return
     */
    MemberVO getMemberById(String memberId);
    /**
     * 회원 정보 수정 service
     * @param memberVO
     * @return
     */
    boolean modifyMember(MemberVO memberVO);

    /**
     * 회원 탈퇴 service
     * @param memberId
     * @param password
     * @return
     */
    boolean deleteMember(String memberId, String password);

    MemberVO getMemberByEmail(String email);


}
