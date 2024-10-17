package com.moijo.gomatch.domain.admin.mapper;

import com.moijo.gomatch.domain.admin.vo.AdminMeetingVO;
import com.moijo.gomatch.domain.member.vo.MemberVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdminMemberMapper {

    /**
     * 관리자 회원 리스트
     * @return
     */
    List<MemberVO> getAllMembers();


    /**
     *  관리자 멤버 정지/삭제
     * @param memberId
     * @return
     */
    boolean deleteMemberByAdmin(String memberId);
    int updateMemberStatus(@Param("memberId") String memberId, @Param("status") String status);

    /**
     * 관리자 검색 기능
     * @param keyword
     * @return
     */
    List<MemberVO> searchMembersById(String keyword);
    List<MemberVO> searchMembersByEmail(String keyword);
    List<MemberVO> searchMembersByName(String keyword);
    List<MemberVO> searchMembersAll(String keyword);


    /**
     * 최근 가입한 5명의 회원 조회
     * @return 최근 가입한 5명의 회원 목록
     */
    List<MemberVO> getRecentMembers();

    List<AdminMeetingVO> getRecentMeetings();
}
