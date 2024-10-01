package com.moijo.gomatch.domain.meeting.service.impl;

import com.moijo.gomatch.domain.game.vo.GameVO;
import com.moijo.gomatch.domain.meeting.mapper.MeetingMapper;
import com.moijo.gomatch.domain.meeting.service.MeetingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MeetingServiceImpl implements MeetingService {
    private final MeetingMapper meetingMapper;

    @Override
    public List<GameVO> getGamesByDate(LocalDate date) {
        return List.of();
    }
}
