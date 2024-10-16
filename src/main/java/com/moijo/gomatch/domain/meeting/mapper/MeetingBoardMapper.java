package com.moijo.gomatch.domain.meeting.mapper;

import com.moijo.gomatch.domain.meeting.vo.MeetingBoardFileVO;
import com.moijo.gomatch.domain.meeting.vo.MeetingBoardReplyVO;
import com.moijo.gomatch.domain.meeting.vo.MeetingBoardVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MeetingBoardMapper {

    // 게시글 등록 메서드
    void insertBoard(MeetingBoardVO meetingBoardVO);
    // 파일 저장 메서드
    int insertBoardFile(MeetingBoardFileVO meetingBoardFileVO);
    void deleteBoard(long meetingBoardNo);
    // 검색 및 필터 조건에 맞는 게시글 수 조회
    int selectBoardCount(@Param("filterType") String filterType, @Param("searchType") String searchType, @Param("keyword") String keyword);

    // 검색 및 필터 조건에 맞는 게시글 리스트 조회
    List<MeetingBoardVO> selectBoardList(@Param("offset") int offset, @Param("pageSize") int pageSize,
                                         @Param("filterType") String filterType, @Param("searchType") String searchType, @Param("keyword") String keyword);


    List<MeetingBoardVO> selectAllBoards();
    MeetingBoardVO selectBoardDetail(long meetingBoardNo);
    List<MeetingBoardReplyVO> selectRepliesByBoardId(long meetingBoardNo);


    int insertReply(@Param("boardNo") long meetingBoardNo, @Param("memberId") String memberId, @Param("meetingReplyContent") String meetingReplyContent);
    int deleteReply(long meetingReplyNo);
    Long selectPreviousPostId(long meetingBoardNo);
    Long selectNextPostId(long meetingBoardNo);
    List<MeetingBoardFileVO> getFilesByMeetingBoardNo(long meetingBoardNo);

    int checkLikeStatus(@Param("meetingBoardNo") long meetingBoardNo, @Param("memberId") String memberId);
    int selectLikeCount(long meetingBoardNo);
    void deleteLike(@Param("meetingBoardNo") long meetingBoardNo, @Param("memberId") String memberId);
    void insertLike(@Param("meetingBoardNo") long meetingBoardNo, @Param("memberId") String memberId);
    void updateMeetingBoardViewCount(long meetingBoardNo); // 조회수 증가 메서드 추가
}
