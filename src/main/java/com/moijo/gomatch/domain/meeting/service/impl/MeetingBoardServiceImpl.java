package com.moijo.gomatch.domain.meeting.service.impl;

import com.moijo.gomatch.domain.meeting.mapper.MeetingBoardMapper;
import com.moijo.gomatch.domain.meeting.service.MeetingBoardService;
import com.moijo.gomatch.domain.meeting.vo.MeetingBoardFileVO;
import com.moijo.gomatch.domain.meeting.vo.MeetingBoardReplyVO;
import com.moijo.gomatch.domain.meeting.vo.MeetingBoardVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MeetingBoardServiceImpl implements MeetingBoardService {

    private final MeetingBoardMapper meetingBoardMapper;

    @Override
    public void addBoard(MeetingBoardVO meetingBoardVO) {
        meetingBoardMapper.insertBoard(meetingBoardVO);
    }
    @Override
    public int getBoardCount(String searchType, String keyword) {
        return meetingBoardMapper.selectBoardCount(searchType, keyword);
    }

    @Override
    public List<MeetingBoardVO> getBoardList(int page, int pageSize, String searchType, String keyword) {
        int offset = (page - 1) * pageSize;
        return meetingBoardMapper.selectBoardList(offset, pageSize, searchType, keyword);
    }
    @Override
    public List<MeetingBoardVO> getAllBoards() {
        return meetingBoardMapper.selectAllBoards();
    }
    @Override
    public MeetingBoardVO getBoardDetail(long meetingBoardNo) {
        return meetingBoardMapper.selectBoardDetail(meetingBoardNo);
    }

    @Override
    public List<MeetingBoardReplyVO> getRepliesByBoardId(long meetingBoardNo) {
        return meetingBoardMapper.selectRepliesByBoardId(meetingBoardNo);
    }

    @Override
    public void likePost(long meetingBoardNo, String memberId) {
        meetingBoardMapper.insertLike(meetingBoardNo, memberId);
    }


    @Override
    public int getLikeCount(long meetingBoardNo) {
        return meetingBoardMapper.selectLikeCount(meetingBoardNo);
    }

    @Override
    public boolean addReply(long meetingBoardNo, String memberId, String content) {
        return meetingBoardMapper.insertReply(meetingBoardNo, memberId, content) > 0;
    }

    @Override
    public Long getPreviousPostId(long meetingBoardNo) {
        return meetingBoardMapper.selectPreviousPostId(meetingBoardNo);
    }

    @Override
    public Long getNextPostId(long meetingBoardNo) {
        return meetingBoardMapper.selectNextPostId(meetingBoardNo);
    }

    @Override
    public List<MeetingBoardFileVO> getMeetingBoardFiles(long meetingBoardNo) {
        return meetingBoardMapper.getFilesByMeetingBoardNo(meetingBoardNo);
    }


    @Override
    public void unlikePost(long meetingBoardNo, String memberId) {
        meetingBoardMapper.deleteLike(meetingBoardNo, memberId);
    }

    @Override
    public boolean checkLikeStatus(long meetingBoardNo, String memberId) {
        return meetingBoardMapper.checkLikeStatus(meetingBoardNo, memberId) > 0;
    }


}
