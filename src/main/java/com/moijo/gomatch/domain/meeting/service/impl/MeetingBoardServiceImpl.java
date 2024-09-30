package com.moijo.gomatch.domain.meeting.service.impl;

import com.moijo.gomatch.domain.meeting.mapper.MeetingBoardMapper;
import com.moijo.gomatch.domain.meeting.service.MeetingBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MeetingBoardServiceImpl implements MeetingBoardService {
    private final MeetingBoardMapper meetingBoardMapper;
}
