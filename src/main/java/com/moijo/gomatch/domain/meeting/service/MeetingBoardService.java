package com.moijo.gomatch.domain.meeting.service;

import com.moijo.gomatch.domain.meeting.vo.MeetingBoardFileVO;
import com.moijo.gomatch.domain.meeting.vo.MeetingBoardVO;

import java.util.List;

public interface MeetingBoardService {

    // 게시글 등록
    void addBoard(MeetingBoardVO meetingBoardVO);

}
