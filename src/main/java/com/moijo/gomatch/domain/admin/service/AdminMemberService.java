package com.moijo.gomatch.domain.admin.service;

import com.moijo.gomatch.domain.admin.vo.AdminMeetingVO;
import com.moijo.gomatch.domain.member.vo.MemberVO;

import java.util.List;

public interface AdminMemberService {

    // 전체 회원 목록 조회
    List<MemberVO> getAllMembers();

    // 회원 계정 정지 처리
    boolean suspendMember(String memberId);

    // 회원 계정 활성화
    boolean activateMember(String memberId);

    // 회원 삭제
    boolean deleteMemberByAdmin(String memberId);

    // 회원관리 페이지 id,email,name,전체 검색
    List<MemberVO> searchMembersById(String searchKeyword);
    List<MemberVO> searchMembersByEmail(String searchKeyword);
    List<MemberVO> searchMembersByName(String searchKeyword);
    List<MemberVO> searchMembersAll(String searchKeyword);

    // 최근 가입 5명 목록 조회
    List<MemberVO> getRecentMembers();

    // 최근 소모임 5개 목록 조회
    List<AdminMeetingVO> getRecentMeetings();


}
