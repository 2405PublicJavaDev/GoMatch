package com.moijo.gomatch.domain.meeting.service.impl;

import com.moijo.gomatch.domain.game.vo.GameVO;
import com.moijo.gomatch.domain.meeting.mapper.MeetingMapper;
import com.moijo.gomatch.domain.meeting.service.MeetingService;
import com.moijo.gomatch.domain.meeting.vo.MeetingAttendVO;
import com.moijo.gomatch.domain.meeting.vo.MeetingFileVO;
import com.moijo.gomatch.domain.meeting.vo.MeetingVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
@Slf4j
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
        return meetingMapper.getGameByNo(gameNo);
    }

    @Override
    public MeetingVO getMeetingsByMeetingNo(long meetingNo) {
        return meetingMapper.getMeetingsByMeetingNo(meetingNo);
    }

    @Override
    public List<MeetingFileVO> getMeetingFileByMeetingNo(long meetingNo) {
        return meetingMapper.getMeetingFileByMeetingNo(meetingNo);
    }

    @Override
    public List<MeetingAttendVO> getMeetingAttendeeByMeetingNo(long meetingNo) {
        return meetingMapper.getMeetingAttendeeByMeetingNo(meetingNo);
    }
    @Override
    public void addAttend(MeetingAttendVO attendVO) {
        meetingMapper.insertMeetingAttend(attendVO);
    }

    @Override
    public boolean checkAlreadyAttended(long meetingNo, String memberId) {
        return meetingMapper.checkMeetingAttend(meetingNo, memberId) > 0;
    }
    @Override
    public void cancelAttend(long meetingNo, String memberId) {
        meetingMapper.deleteMeetingAttend(meetingNo, memberId);
    }

    @Override
    public void removeMeeting(long meetingNo) {
        meetingMapper.deleteMeeting(meetingNo);
    }
    @Override
    public void modifyMeeting(MeetingVO meetingVO) {
        meetingMapper.updateMeeting(meetingVO);
    }

    @Override
    public void deleteMeetingFiles(List<Long> fileDeleteIds) {
        log.info("MeetingServiceImpl - 삭제할 파일 ID 목록: {}", fileDeleteIds);
        if (fileDeleteIds != null && !fileDeleteIds.isEmpty()) {
            meetingMapper.deleteMeetingFiles(fileDeleteIds);
        }
    }
    @Override
    public List<String> getAllGameDates() {
        return meetingMapper.getAllGameDates();
    }
}

