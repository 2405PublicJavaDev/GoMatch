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

    MeetingBoardVO getBoardDetail(long meetingBoardNo);

    List<MeetingBoardReplyVO> getRepliesByBoardId(long meetingBoardNo); // getCommentsByBoardId -> getRepliesByBoardId로 수정

    int getBoardCount(String searchType, String keyword);

    List<MeetingBoardVO> getBoardList(int page, int pageSize, String searchType, String keyword);

    List<MeetingBoardVO> getAllBoards();

    void likePost(long meetingBoardNo, String memberId);
    void unlikePost(long meetingBoardNo, String memberId);
    boolean checkLikeStatus(long meetingBoardNo, String memberId);
    int getLikeCount(long meetingBoardNo);


    boolean addReply(long meetingBoardNo, String memberId, String content); // addComment -> addReply로 수정

    Long getPreviousPostId(long meetingBoardNo);

    Long getNextPostId(long meetingBoardNo);

    List<MeetingBoardFileVO> getMeetingBoardFiles(long meetingBoardNo);

    default String markdownToHtml(String markdownContent) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(markdownContent);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(document);
    }
    void increaseViewCount(long meetingBoardNo); // 조회수 증가 메서드 추가

}
