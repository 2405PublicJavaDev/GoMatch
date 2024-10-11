package com.moijo.gomatch.domain.member.service;

import com.moijo.gomatch.domain.member.vo.MemberVO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

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
    boolean isIdDuplicate(String memberId);
    boolean isEmailDuplicate(String memberEmail);
    boolean isNicknameDuplicate(String memberNickname);

    /**
     * 아이디 찾기 service
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

    boolean checkPassword(String memberId, String password);
    String uploadProfileImage(MultipartFile file);
    MemberVO getMemberById(String memberId);
    boolean modifyMember(MemberVO memberVO);


    boolean deleteMember(String memberId, String password);

}
