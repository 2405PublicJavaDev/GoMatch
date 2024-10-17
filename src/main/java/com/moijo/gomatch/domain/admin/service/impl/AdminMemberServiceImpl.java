package com.moijo.gomatch.domain.admin.service.impl;

import com.moijo.gomatch.domain.admin.mapper.AdminMemberMapper;
import com.moijo.gomatch.domain.admin.service.AdminMemberService;
import com.moijo.gomatch.domain.admin.vo.AdminMeetingVO;
import com.moijo.gomatch.domain.member.vo.MemberVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminMemberServiceImpl implements AdminMemberService {

    private final AdminMemberMapper aMapper;


    // 관리자 멤버 리스트
    @Override
    public List<MemberVO> getAllMembers() {
        return aMapper.getAllMembers();
    }

    // 관리자 멤버 삭제/ 정지
    @Override
    public boolean suspendMember(String memberId) {
        return aMapper.updateMemberStatus(memberId, "SUSPENDED") > 0;
    }

    @Override
    public boolean activateMember(String memberId) {
        return aMapper.updateMemberStatus(memberId, "NORMAL") > 0;
    }

    @Override
    public boolean deleteMemberByAdmin(String memberId) {
        return aMapper.deleteMemberByAdmin(memberId);
    }


    // 관리자 멤버 검색
    @Override
    public List<MemberVO> searchMembersById(String keyword) {
        return aMapper.searchMembersById("%" + keyword + "%");
    }

    @Override
    public List<MemberVO> searchMembersByEmail(String keyword) {
        return aMapper.searchMembersByEmail("%" + keyword + "%");
    }



    @Override
    public List<MemberVO> searchMembersByName(String keyword) {
        return aMapper.searchMembersByName("%" + keyword + "%");
    }

    @Override
    public List<MemberVO> searchMembersAll(String keyword) {
        return aMapper.searchMembersAll("%" + keyword + "%");
    }

    @Override
    public List<MemberVO> getRecentMembers() {
        return aMapper.getRecentMembers();
    }

    @Override
    public List<AdminMeetingVO> getRecentMeetings() {
        return aMapper.getRecentMeetings();
    }

}
