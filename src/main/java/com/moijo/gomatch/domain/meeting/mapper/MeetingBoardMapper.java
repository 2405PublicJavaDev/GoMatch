package com.moijo.gomatch.domain.meeting.mapper;

import com.moijo.gomatch.domain.meeting.vo.MeetingBoardFileVO;
import com.moijo.gomatch.domain.meeting.vo.MeetingBoardLikeVO;
import com.moijo.gomatch.domain.meeting.vo.MeetingBoardReplyVO;
import com.moijo.gomatch.domain.meeting.vo.MeetingBoardVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface MeetingBoardMapper {

    // ■■■■■■■■■■■■■■■■■■■ 나의 소모임 (MyMeeting) ■■■■■■■■■■■■■■■■■■■■ //

    /**
     * 내가 작성한 게시글 리스트를 조회하는 메서드
     * @param memberId 조회할 회원 ID
     * @return 회원이 작성한 게시글 리스트
     */
    List<MeetingBoardVO> selectBoardsByMemberId(String memberId);

    /**
     * 내가 작성한 댓글 리스트를 조회하는 메서드
     * @param memberId 조회할 회원 ID
     * @return 회원이 작성한 댓글 리스트
     */
    List<MeetingBoardReplyVO> selectBoardRepliesByMemberId(String memberId);

    /**
     * 내가 좋아요 한 게시글 리스트를 조회하는 메서드
     * @param memberId 조회할 회원 ID
     * @return 회원이 좋아요 한 게시글 리스트
     */
    List<MeetingBoardLikeVO> selectBoardLikesByMemberId(String memberId);

    // ■■■■■■■■■■■■■■■■■■■ 게시글 조회 및 검색 ■■■■■■■■■■■■■■■■■■■■ //

    /**
     * 게시글 상세 정보를 조회하는 메서드
     * @param meetingBoardNo 조회할 게시글 번호
     * @return 게시글 상세 정보
     */
    MeetingBoardVO selectBoardDetail(long meetingBoardNo);

    /**
     * 게시글에 포함된 파일 리스트를 조회하는 메서드
     * @param meetingBoardNo 게시글 번호
     * @return 게시글에 포함된 파일 리스트
     */
    List<MeetingBoardFileVO> getFilesByMeetingBoardNo(long meetingBoardNo);

    /**
     * 검색 및 필터 조건에 맞는 게시글 수를 조회하는 메서드
     * @param filterType 필터 타입
     * @param searchType 검색 타입
     * @param keyword 검색 키워드
     * @return 조건에 맞는 게시글 수
     */
    int selectBoardCount(@Param("filterType") String filterType, @Param("searchType") String searchType, @Param("keyword") String keyword);

    /**
     * 검색 및 필터 조건에 맞는 게시글 리스트를 조회하는 메서드
     * @param offset 페이지 오프셋
     * @param pageSize 페이지 크기
     * @param filterType 필터 타입
     * @param searchType 검색 타입
     * @param keyword 검색 키워드
     * @return 조건에 맞는 게시글 리스트
     */
    List<MeetingBoardVO> selectBoardList(@Param("offset") int offset, @Param("pageSize") int pageSize,
                                         @Param("filterType") String filterType, @Param("searchType") String searchType, @Param("keyword") String keyword);

    /**
     * 모든 게시글 리스트를 조회하는 메서드
     * @return 게시글 리스트
     */
    List<MeetingBoardVO> selectAllBoards();

    /**
     * 이전 게시글 번호를 조회하는 메서드
     * @param meetingBoardNo 조회할 게시글 번호
     * @return 이전 게시글 번호
     */
    Long selectPreviousPostId(long meetingBoardNo);

    /**
     * 다음 게시글 번호를 조회하는 메서드
     * @param meetingBoardNo 조회할 게시글 번호
     * @return 다음 게시글 번호
     */
    Long selectNextPostId(long meetingBoardNo);

    /**
     * 게시글 좋아요 수를 조회하는 메서드
     * @param meetingBoardNo 게시글 번호
     * @return 게시글의 좋아요 수
     */
    int selectLikeCount(long meetingBoardNo);

    /**
     * 특정 게시글의 댓글 리스트를 조회하는 메서드
     * @param meetingBoardNo 게시글 번호
     * @return 게시글에 달린 댓글 리스트
     */
    List<MeetingBoardReplyVO> selectRepliesByBoardId(long meetingBoardNo);

    // ■■■■■■■■■■■■■■■■■■■ 게시글 등록/수정/삭제 (CRUD) ■■■■■■■■■■■■■■■■■■■■ //

    /**
     * 게시글을 등록하는 메서드
     * @param meetingBoardVO 게시글 정보
     */
    void insertBoard(MeetingBoardVO meetingBoardVO);

    /**
     * 게시글을 삭제하는 메서드
     * @param meetingBoardNo 게시글 번호
     */
    void deleteBoard(long meetingBoardNo);

    /**
     * 게시글을 수정하는 메서드
     * @param meetingBoardVO 게시글 정보
     */
    void updateBoard(MeetingBoardVO meetingBoardVO);

    /**
     * 게시글에 포함된 파일을 삭제하는 메서드
     * @param fileDeleteIds 삭제할 파일 ID 리스트
     */
    void deleteBoardFiles(@Param("fileDeleteIds") List<Long> fileDeleteIds);

    /**
     * 게시글에 포함된 파일을 저장하는 메서드
     * @param meetingBoardFileVO 파일 정보
     * @return 저장된 파일 수
     */
    int insertBoardFile(MeetingBoardFileVO meetingBoardFileVO);

    /**
     * 게시글 조회수를 증가시키는 메서드
     * @param meetingBoardNo 게시글 번호
     */
    void updateMeetingBoardViewCount(long meetingBoardNo);

    // ■■■■■■■■■■■■■■■■■■■ 게시글 좋아요 ■■■■■■■■■■■■■■■■■■■■ //

    /**
     * 게시글에 좋아요를 추가하는 메서드
     * @param meetingBoardNo 게시글 번호
     * @param memberId 회원 ID
     */
    void insertLike(@Param("meetingBoardNo") long meetingBoardNo, @Param("memberId") String memberId);

    /**
     * 게시글에 좋아요를 취소하는 메서드
     * @param meetingBoardNo 게시글 번호
     * @param memberId 회원 ID
     */
    void deleteLike(@Param("meetingBoardNo") long meetingBoardNo, @Param("memberId") String memberId);

    /**
     * 게시글의 좋아요 여부를 확인하는 메서드
     * @param meetingBoardNo 게시글 번호
     * @param memberId 회원 ID
     * @return 좋아요 여부 (1: 좋아요, 0: 좋아요 아님)
     */
    int checkLikeStatus(@Param("meetingBoardNo") long meetingBoardNo, @Param("memberId") String memberId);

    // ■■■■■■■■■■■■■■■■■■■ 게시글 댓글 ■■■■■■■■■■■■■■■■■■■■ //

    /**
     * 게시글에 댓글을 등록하는 메서드
     * @param meetingBoardNo 게시글 번호
     * @param memberId 회원 ID
     * @param meetingReplyContent 댓글 내용
     * @return 등록된 댓글 수
     */
    int insertReply(@Param("boardNo") long meetingBoardNo, @Param("memberId") String memberId, @Param("meetingReplyContent") String meetingReplyContent);

    /**
     * 게시글의 댓글을 삭제하는 메서드
     * @param meetingReplyNo 댓글 번호
     * @return 삭제된 댓글 수
     */
    int deleteReply(long meetingReplyNo);
}