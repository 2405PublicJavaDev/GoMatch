package com.moijo.gomatch.domain.meeting.service.impl;

import com.moijo.gomatch.domain.meeting.mapper.MeetingBoardReplyMapper;
import com.moijo.gomatch.domain.meeting.service.MeetingBoardReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MeetingBoardReplyServiceImpl implements MeetingBoardReplyService {

    private final MeetingBoardReplyMapper meetingBoardReplyMapper;
}
