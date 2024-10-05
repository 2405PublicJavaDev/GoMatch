package com.moijo.gomatch.domain.meeting.service.impl;

import com.moijo.gomatch.domain.game.vo.GameVO;
import com.moijo.gomatch.domain.meeting.mapper.MeetingMapper;
import com.moijo.gomatch.domain.meeting.service.MeetingService;
import com.moijo.gomatch.domain.meeting.vo.MeetingVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MeetingServiceImpl implements MeetingService {
    private final MeetingMapper meetingMapper;

    @Override
    public List<GameVO> getGamesByDate(String date) {  // LocalDate 대신 String 사용
        return meetingMapper.getGamesByDate(date);
    }

    @Override
    public void addMeeting(MeetingVO meetingVO) {
        // 현재 날짜와 시간을 등록 날짜로 설정
        meetingVO.setRegDate(new Timestamp(System.currentTimeMillis()));
        meetingMapper.insertMeeting(meetingVO); // DB에 등록
    }
    @Override
    public List<MeetingVO> getMeetingsByDate(String date) {
        return meetingMapper.getMeetingsByDate(date);
    }
    @Override
    public List<MeetingVO> getMeetingsByDateAndTeam(String date, String team) {
        return meetingMapper.getMeetingsByDateAndTeam(date, team);
    }
    @Override
    public GameVO getGameByNo(int gameNo) {
        return meetingMapper.getGameByNo(gameNo); // Mapper에서 해당 gameNo로 경기 정보를 조회
    }

}

