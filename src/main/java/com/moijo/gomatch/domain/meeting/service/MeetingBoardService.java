package com.moijo.gomatch.domain.meeting.service;

import com.moijo.gomatch.domain.meeting.vo.MeetingBoardFileVO;
import com.moijo.gomatch.domain.meeting.vo.MeetingBoardReplyVO;
import com.moijo.gomatch.domain.meeting.vo.MeetingBoardVO;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.util.List;

public interface MeetingBoardService {

    void addBoard(MeetingBoardVO meetingBoardVO);
    void removeBoard(long meetingBoardNo);
    MeetingBoardVO getBoardDetail(long meetingBoardNo);
    // 검색 조건에 맞는 게시글 수 조회
    int getBoardCount(String filterType, String searchType, String keyword);
    // 검색 조건에 맞는 게시글 조회
    List<MeetingBoardVO> getBoardList(int page, int pageSize, String filterType, String searchType, String keyword);
    List<MeetingBoardVO> getAllBoards();

    void likePost(long meetingBoardNo, String memberId);
    void unlikePost(long meetingBoardNo, String memberId);
    boolean checkLikeStatus(long meetingBoardNo, String memberId);
    int getLikeCount(long meetingBoardNo);

    List<MeetingBoardReplyVO> getRepliesByBoardId(long meetingBoardNo);
    boolean addReply(long meetingBoardNo, String memberId, String meetingReplyContent);
    boolean deleteReply(long meetingReplyNo);
    Long getPreviousPostId(long meetingBoardNo);

    Long getNextPostId(long meetingBoardNo);

    List<MeetingBoardFileVO> getMeetingBoardFiles(long meetingBoardNo);

    default String markdownToHtml(String markdownContent) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(markdownContent);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(document);
    }
    // 조회수 증가
    void increaseViewCount(long meetingBoardNo);
    void modifyBoard(MeetingBoardVO meetingBoardVO);
    void deleteBoardFiles(List<Long> fileDDeleteIds);
}
