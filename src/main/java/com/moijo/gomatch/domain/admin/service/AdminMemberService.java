package com.moijo.gomatch.domain.admin.service;

import com.moijo.gomatch.domain.admin.vo.AdminMeetingVO;
import com.moijo.gomatch.domain.member.vo.MemberVO;

import java.util.List;

public interface AdminMemberService {

    /**
     * 관리자 멤버 관리
     * @return
     */
    List<MemberVO> getAllMembers();

    /**
     *  관리자 멤버 삭제/정지
     * @param memberId
     * @return
     */
    boolean suspendMember(String memberId);
    boolean activateMember(String memberId);
    boolean deleteMemberByAdmin(String memberId);


    /**
     * 관리자 회원 검색
     * @param searchKeyword
     * @return
     */
    List<MemberVO> searchMembersById(String searchKeyword);
    List<MemberVO> searchMembersByEmail(String searchKeyword);
    List<MemberVO> searchMembersByName(String searchKeyword);
    List<MemberVO> searchMembersAll(String searchKeyword);


    /**
     * 최근 가입한 5명의 회원 조회
     * @return 최근 가입한 5명의 회원 목록
     */
    List<MemberVO> getRecentMembers();

    List<AdminMeetingVO> getRecentMeetings();


}
