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
    int selectBoardCount(@Param("searchType") String searchType, @Param("keyword") String keyword);
    List<MeetingBoardVO> selectBoardList(@Param("offset") int offset, @Param("pageSize") int pageSize,
                                         @Param("searchType") String searchType, @Param("keyword") String keyword);

    List<MeetingBoardVO> selectAllBoards();
    MeetingBoardVO selectBoardDetail(long meetingBoardNo);
    List<MeetingBoardReplyVO> selectRepliesByBoardId(long meetingBoardNo);


    int insertReply(@Param("boardNo") long meetingBoardNo, @Param("memberId") String memberId, @Param("content") String content);
    Long selectPreviousPostId(long meetingBoardNo);
    Long selectNextPostId(long meetingBoardNo);
    List<MeetingBoardFileVO> getFilesByMeetingBoardNo(long meetingBoardNo);

    int checkLikeStatus(@Param("meetingBoardNo") long meetingBoardNo, @Param("memberId") String memberId);
    int selectLikeCount(long meetingBoardNo);
    void deleteLike(@Param("meetingBoardNo") long meetingBoardNo, @Param("memberId") String memberId);
    void insertLike(@Param("meetingBoardNo") long meetingBoardNo, @Param("memberId") String memberId);
}
