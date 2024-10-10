package com.moijo.gomatch.domain.meeting.service;

import com.moijo.gomatch.domain.game.vo.GameVO;
import com.moijo.gomatch.domain.meeting.vo.MeetingAttendVO;
import com.moijo.gomatch.domain.meeting.vo.MeetingFileVO;
import com.moijo.gomatch.domain.meeting.vo.MeetingVO;

import java.util.List;

public interface MeetingService {

    // 날짜에 따른 게임 정보 리스트 불러오기
    List<GameVO> getGamesByDate(String date);
    // 소모임 등록
    void addMeeting(MeetingVO meetingVO);
    // 날짜별 소모임 리스트 불러오기
    List<MeetingVO> getMeetingsByDate(String date);
    // 날짜별 팀별 리스트 불러오기
    List<MeetingVO> getMeetingsByDateAndTeam(String date, String team);
    // gameNo로 경기 정보를 조회하는 메서드
    GameVO getGameByNo(int gameNo);
    // 소모임 번호로 소모임 정보 조회
    MeetingVO getMeetingsByMeetingNo(long meetingNo);
    // 소모임 번호로 파일 정보 조회
    List<MeetingFileVO> getMeetingFileByMeetingNo(long meetingNo);
    // 소모임 번호로 참석자 정보 조회
    List<MeetingAttendVO> getMeetingAttendeeByMeetingNo(long meetingNo);
    // 소모임 참석자 등록
    void addAttend(MeetingAttendVO attendVO);
    // 이미 참석했는지 확인하는 메서드
    boolean checkAlreadyAttended(long meetingNo, String memberId);
    // 참석 취소
    void cancelAttend(long meetingNo, String memberId);
    // 소모임 삭제
    void removeMeeting(long meetingNo);
    void updateMeeting(MeetingVO meetingVO);
    void deleteMeetingFiles(List<Long> fileDeleteIds);
}