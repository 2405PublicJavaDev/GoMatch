package com.moijo.gomatch.domain.member.service.impl;

import com.moijo.gomatch.domain.member.common.EmailService;
import com.moijo.gomatch.domain.member.mapper.MemberMapper;
import com.moijo.gomatch.domain.member.service.MemberService;
import com.moijo.gomatch.domain.member.vo.MemberVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService {

    private final MemberMapper mapper;
    private final EmailService emailService;


    // 로그인 처리
    @Override
    public MemberVO checkLogin(MemberVO member) {
        MemberVO result = mapper.checkLogin(member);
        if (result != null && result.getMemberPw().equals(member.getMemberPw())) {
            return result;
        }
        return null;
    }

    // 회원가입
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

    // 아이디 중복체크
    @Override
    public boolean isIdDuplicate(String memberId) {
        return mapper.countByMemberId(memberId) > 0;
    }

    // 이메일 중복체크
    @Override
    public boolean isEmailDuplicate(String memberEmail) {
        return mapper.countByMemberEmail(memberEmail) > 0;
    }

    // 닉네임 중복체크
    @Override
    public boolean isNicknameDuplicate(String memberNickname) {
        return mapper.countByMemberNickname(memberNickname) > 0;
    }

    // 아이디 찾기(아이디,생년월일)
    @Override
    public String findIdByNameAndBirthDate(String name, String birthDate) {
        // birthDate는 이미 'yyyy-MM-dd' 형식으로 입력된다고 가정합니다.
        return mapper.findIdByNameAndBirthDate(name, birthDate);
    }


    // 비밀번호찾기( 임시 비밀번호 전송)
    @Override
    public boolean resetPassword(String memberId, String email) {
        MemberVO member = mapper.findByIdAndEmail(memberId, email);
        if (member == null) {
            return false;
        }
        String tempPassword = generateTempPassword();
        mapper.updatePassword(memberId, tempPassword);
        emailService.sendTempPassword(email, tempPassword);
        return true;
    }

    // 임시 비밀번호 생성
    private String generateTempPassword() {
        return UUID.randomUUID().toString().substring(0, 8);
    }


    // 프로필 이미지 등록(서버)
    @Override
    public String uploadProfileImage(MultipartFile file) {
        try {
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path uploadPath = Paths.get("uploads");
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            return "/uploads/" + fileName;
        } catch (IOException e) {
            log.error("Error uploading profile image", e);
            throw new RuntimeException("프로필 이미지 업로드 중 오류 발생", e);
        }
    }

    // 회원 정보 조회
    @Override
    public MemberVO getMemberById(String memberId) {
        return mapper.getMemberById(memberId);
    }


    // 회원 정보 수정
    @Override
    @Transactional
    public boolean modifyMember(MemberVO memberVO) {
        try {
            // 비밀번호가 변경되었다면, 여기서 새 비밀번호를 설정합니다.
            // 주의: 실제 운영 환경에서는 비밀번호를 암호화해야 합니다.
            int result = mapper.modifyMember(memberVO);
            return result > 0;
        } catch (Exception e) {
            log.error("Error modifying member: {}", memberVO.getMemberId(), e);
            return false;
        }
    }

    // 회원 탈퇴
    @Transactional
    public boolean deleteMember(String memberId, String password) {
        MemberVO member = mapper.getMemberById(memberId);
        if (member == null || !member.getMemberPw().equals(password)) {
            return false;
        }

        try {
            // 회원 랭크 정보 삭제
            mapper.deleteMemberRank(memberId);

            // 기타 관련 데이터 삭제
            // mapper.deleteMemberPosts(memberId);
            // mapper.deleteMemberComments(memberId);
            // 등등...

            // 프로필 이미지 삭제
            deleteProfileImage(member.getProfileImageUrl());

            // 회원 정보 삭제
            mapper.deleteMember(memberId);

            log.info("회원 탈퇴 완료: {}", memberId);
            return true;
        } catch (Exception e) {
            log.error("회원 탈퇴 중 오류 발생: {}", memberId, e);
            throw new RuntimeException("회원 탈퇴 처리 중 오류가 발생했습니다.", e);
        }
    }


    // 회원탈퇴시 이미지 삭제
    private void deleteProfileImage(String profileImageUrl) {
        if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
            try {
                Path imagePath = Paths.get("src/main/resources/static" + profileImageUrl);
                Files.deleteIfExists(imagePath);
            } catch (IOException e) {
                log.error("프로필 이미지 삭제 중 오류 발생: {}", profileImageUrl, e);
            }
        }
    }

    // 비밀번호 일치 확인
    @Override
    public boolean checkPassword(String memberId, String password) {
        MemberVO member = mapper.getMemberById(memberId);
        if (member != null) {
            boolean isMatch = password.equals(member.getMemberPw());
            log.info("Password check for user {}: {}. Input: {}, Stored: {}",
                    memberId, isMatch, password, member.getMemberPw());
            return isMatch;
        }
        log.warn("User not found for password check: {}", memberId);
        return false;
    }


    @Override
    public MemberVO getMemberByEmail(String email) {
        return mapper.getMemberByEmail(email);
    }



}
