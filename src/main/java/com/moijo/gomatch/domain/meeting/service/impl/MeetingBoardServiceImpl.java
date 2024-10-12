package com.moijo.gomatch.domain.meeting.service.impl;

import com.moijo.gomatch.domain.meeting.mapper.MeetingBoardMapper;
import com.moijo.gomatch.domain.meeting.service.MeetingBoardService;
import com.moijo.gomatch.domain.meeting.vo.MeetingBoardFileVO;
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


}
