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
    // ■■■■■■■■■■■■■■■■■■■ 나의 소모임 (MyMeeting) ■■■■■■■■■■■■■■■■■■■■ //
    // 내가 개설한 소모임 리스트 불러오기
    @Override
    public List<MeetingVO> getMeetingsByMemberId(String memberId) {
        return meetingMapper.getMeetingsByMemberId(memberId);
    }
    // 내가 참석한 소모임 리스트 불러오기
    @Override
    public List<MeetingAttendVO> getMeetingAttendsByMemberId(String memberId) {
        return meetingMapper.getMeetingAttendsByMemberId(memberId);
    }

    // ■■■■■■■■■■■■■■■■■■■ 소모임 조회 (getMeeting) ■■■■■■■■■■■■■■■■■■■■ //
    // 날짜에 따른 게임 정보 리스트 불러오기
    @Override
    public List<GameVO> getGamesByDate(String date) {
        return meetingMapper.getGamesByDate(date);
    }

    // 날짜별 소모임 리스트 불러오기
    @Override
    public List<MeetingVO> getMeetingsByDate(String date) {
        return meetingMapper.getMeetingsByDate(date);
    }

    // 날짜별 팀별 소모임 리스트 불러오기
    @Override
    public List<MeetingVO> getMeetingsByDateAndTeam(String date, String team) {
        return meetingMapper.getMeetingsByDateAndTeam(date, team);
    }

    // gameNo로 경기 정보를 조회하는 메서드
    @Override
    public GameVO getGameByNo(int gameNo) {
        return meetingMapper.getGameByNo(gameNo);
    }

    // 소모임 번호로 소모임 정보 조회
    @Override
    public MeetingVO getMeetingsByMeetingNo(long meetingNo) {
        return meetingMapper.getMeetingsByMeetingNo(meetingNo);
    }

    // 소모임 번호로 파일 정보 조회
    @Override
    public List<MeetingFileVO> getMeetingFileByMeetingNo(long meetingNo) {
        return meetingMapper.getMeetingFileByMeetingNo(meetingNo);
    }

    // 소모임 번호로 참석자 정보 조회
    @Override
    public List<MeetingAttendVO> getMeetingAttendeeByMeetingNo(long meetingNo) {
        return meetingMapper.getMeetingAttendeeByMeetingNo(meetingNo);
    }

    // 모든 경기 날짜 불러오기
    @Override
    public List<String> getAllGameDates() {
        return meetingMapper.getAllGameDates();
    }

    // ■■■■■■■■■■■■■■■■■■■ 소모임 기능 (Meeting) ■■■■■■■■■■■■■■■■■■■■ //

    // 소모임 등록
    @Override
    public void addMeeting(MeetingVO meetingVO) {
        meetingVO.setRegDate(new Timestamp(System.currentTimeMillis()));
        meetingMapper.insertMeeting(meetingVO);
    }

    // 소모임 수정
    @Override
    public void modifyMeeting(MeetingVO meetingVO) {
        meetingMapper.updateMeeting(meetingVO);
    }

    // 소모임 삭제
    @Override
    public void removeMeeting(long meetingNo) {
        meetingMapper.deleteMeeting(meetingNo);
    }

    // 소모임 파일 삭제
    @Override
    public void deleteMeetingFiles(List<Long> fileDeleteIds) {
        log.info("MeetingServiceImpl - 삭제할 파일 ID 목록: {}", fileDeleteIds);
        if (fileDeleteIds != null && !fileDeleteIds.isEmpty()) {
            meetingMapper.deleteMeetingFiles(fileDeleteIds);
        }
    }

    // ■■■■■■■■■■■■■■■ 소모임 참석/취소 (MeetingAttend) ■■■■■■■■■■■■■■ //

    // 이미 참석했는지 확인하는 메서드
    @Override
    public boolean checkAlreadyAttended(long meetingNo, String memberId) {
        return meetingMapper.checkMeetingAttend(meetingNo, memberId) > 0;
    }

    // 소모임 참석자 등록
    @Override
    public void addAttend(MeetingAttendVO attendVO) {
        meetingMapper.insertMeetingAttend(attendVO);
    }

    // 참석 취소
    @Override
    public void cancelAttend(long meetingNo, String memberId) {
        meetingMapper.deleteMeetingAttend(meetingNo, memberId);
    }
}
