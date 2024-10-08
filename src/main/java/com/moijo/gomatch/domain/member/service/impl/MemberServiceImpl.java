package com.moijo.gomatch.domain.member.service.impl;

import com.moijo.gomatch.domain.member.common.EmailService;
import com.moijo.gomatch.domain.member.mapper.MemberMapper;
import com.moijo.gomatch.domain.member.service.MemberService;
import com.moijo.gomatch.domain.member.vo.MemberVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Member;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberMapper mapper;
    private final EmailService emailService;

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

    @Override
    public String findIdByNameAndBirthDate(String name, String birthDate) {
        // birthDate는 이미 'yyyy-MM-dd' 형식으로 입력된다고 가정합니다.
        return mapper.findIdByNameAndBirthDate(name, birthDate);
    }

    private String formatBirthDate(String birthDate) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyyMMdd");
            SimpleDateFormat outputFormat = new SimpleDateFormat("yy/MM/dd");
            Date date = inputFormat.parse(birthDate);
            return outputFormat.format(date);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid birth date format", e);
        }
    }

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

    private String generateTempPassword() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

}
