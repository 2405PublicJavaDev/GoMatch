package com.moijo.gomatch.domain.meeting.mapper;


import com.moijo.gomatch.domain.game.vo.GameVO;
import com.moijo.gomatch.domain.meeting.vo.MeetingAttendVO;
import com.moijo.gomatch.domain.meeting.vo.MeetingFileVO;
import com.moijo.gomatch.domain.meeting.vo.MeetingVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MeetingMapper {

    // 날짜별 경기를 조회하는 메서드
    List<GameVO> getGamesByDate(@Param("date") String date);  // LocalDate 대신 String 사용
    // 소모임 등록을 위한 메서드
    void insertMeeting(MeetingVO meetingVO);
    // 파일 정보를 삽입하는 메서드
    int insertMeetingFile(MeetingFileVO meetingFileVO);
    // 날짜별 소모임 리스트를 조회
    List<MeetingVO> getMeetingsByDate(@Param("date") String date);
    // 날짜/팀별 소모임 리스트 조회
    List<MeetingVO> getMeetingsByDateAndTeam(@Param("date") String date, @Param("team") String team);
    // gameNo로 경기 정보를 조회하는 메서드
    GameVO getGameByNo(@Param("gameNo") int gameNo);
    // 소모임 번호로 소모임 정보 조회
    MeetingVO getMeetingsByMeetingNo(@Param("meetingNo") long meetingNo);
    // 소모임 번호로 파일 정보 조회
    List<MeetingFileVO> getMeetingFileByMeetingNo(@Param("meetingNo") long meetingNo);
    // 소모임 번호로 참석자 정보 조회
    List<MeetingAttendVO> getMeetingAttendeeByMeetingNo(@Param("meetingNo") long meetingNo);
    // 소모임 참석 등록
    void insertMeetingAttend(MeetingAttendVO attendVO);
    // 소모임 참석 여부 확인
    int checkMeetingAttend(@Param("meetingNo") long meetingNo, @Param("memberId") String memberId);
    // 참석 취소 메서드 추가
    void deleteMeetingAttend(@Param("meetingNo") long meetingNo, @Param("memberId") String memberId);
    // 소모임 삭제
    void deleteMeeting(long meetingNo);
    void updateMeeting(MeetingVO meetingVO);
    void deleteMeetingFiles(@Param("fileDeleteIds") List<Long> fileDeleteIds);
    List<String> getAllGameDates();

}
