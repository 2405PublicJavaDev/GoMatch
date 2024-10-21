package com.moijo.gomatch.domain.meeting.service;

import com.moijo.gomatch.domain.meeting.vo.MeetingBoardFileVO;
import com.moijo.gomatch.domain.meeting.vo.MeetingBoardLikeVO;
import com.moijo.gomatch.domain.meeting.vo.MeetingBoardReplyVO;
import com.moijo.gomatch.domain.meeting.vo.MeetingBoardVO;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import java.util.List;

public interface MeetingBoardService {

    // ■■■■■■■■■■■■■■■■■■■ 나의 소모임 (MyMeeting) ■■■■■■■■■■■■■■■■■■■■ //
    // 내가 작성한 게시글 리스트 불러오기
    List<MeetingBoardVO> getBoardsByMemberId(String meetingId);
    // 내가 작성한 댓글 리스트 불러오기
    List<MeetingBoardReplyVO> getBoardRepliesByMemberId(String meetingId);
    // 내가 좋아요 한 게시글 리스트 불러오기
    List<MeetingBoardLikeVO> getBoardLikesByMemberId(String meetingId);

    // ■■■■■■■■■■■■■■■■■■■ 게시글 조회 및 검색 ■■■■■■■■■■■■■■■■■■■■ //
    // 게시글 상세 정보 조회
    MeetingBoardVO getBoardDetail(long meetingBoardNo);
    // 게시글 파일 리스트 조회
    List<MeetingBoardFileVO> getMeetingBoardFiles(long meetingBoardNo);
    // 검색 조건에 맞는 게시글 수 조회
    int getBoardCount(String filterType, String searchType, String keyword);
    // 검색 조건에 맞는 게시글 리스트 조회
    List<MeetingBoardVO> getBoardList(int page, int pageSize, String filterType, String searchType, String keyword);
    // 모든 게시글 리스트 조회
    List<MeetingBoardVO> getAllBoards();
    // 이전 게시글 번호 조회
    Long getPreviousPostId(long meetingBoardNo);
    // 다음 게시글 번호 조회
    Long getNextPostId(long meetingBoardNo);
    // 마크다운 텍스트를 HTML로 변환
    default String markdownToHtml(String markdownContent) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(markdownContent);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(document);
    }
    // 게시글 좋아요 수 조회
    int getLikeCount(long meetingBoardNo);
    // 특정 게시글의 댓글 리스트 조회
    List<MeetingBoardReplyVO> getRepliesByBoardId(long meetingBoardNo);

    // ■■■■■■■■■■■■■■■■■■■ 게시글 등록/수정/삭제 (CRUD) ■■■■■■■■■■■■■■■■■■■■ //
    // 게시글 등록
    void addBoard(MeetingBoardVO meetingBoardVO);
    // 게시글 삭제
    void removeBoard(long meetingBoardNo);
    // 게시글 수정
    void modifyBoard(MeetingBoardVO meetingBoardVO);
    // 게시글 파일 삭제
    void deleteBoardFiles(List<Long> fileDeleteIds);
    // 게시글 조회수 증가
    void increaseViewCount(long meetingBoardNo);

    // ■■■■■■■■■■■■■■■■■■■ 게시글 좋아요 ■■■■■■■■■■■■■■■■■■■■ //
    // 게시글 좋아요 처리
    void likePost(long meetingBoardNo, String memberId);
    // 게시글 좋아요 취소 처리
    void unlikePost(long meetingBoardNo, String memberId);
    // 게시글 좋아요 여부 확인
    boolean checkLikeStatus(long meetingBoardNo, String memberId);

    // ■■■■■■■■■■■■■■■■■■■ 게시글 댓글 ■■■■■■■■■■■■■■■■■■■■ //
    // 게시글에 댓글 등록
    boolean addReply(long meetingBoardNo, String memberId, String meetingReplyContent);
    // 게시글 댓글 삭제
    boolean deleteReply(long meetingReplyNo);

}