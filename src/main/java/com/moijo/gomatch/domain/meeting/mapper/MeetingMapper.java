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

    // ■■■■■■■■■■■■■■■■■■■ 소모임 조회 (getMeeting) ■■■■■■■■■■■■■■■■■■■■ //

    /**
     * 날짜별 경기를 조회하는 메서드
     * @param date 조회할 날짜
     * @return 경기 정보 리스트
     */
    List<GameVO> getGamesByDate(@Param("date") String date);

    /**
     * 날짜별 소모임 리스트를 조회하는 메서드
     * @param date 조회할 날짜
     * @return 소모임 리스트
     */
    List<MeetingVO> getMeetingsByDate(@Param("date") String date);

    /**
     * 날짜와 팀별 소모임 리스트 조회하는 메서드
     * @param date 조회할 날짜
     * @param team 팀 이름
     * @return 소모임 리스트
     */
    List<MeetingVO> getMeetingsByDateAndTeam(@Param("date") String date, @Param("team") String team);

    /**
     * gameNo로 경기 정보를 조회하는 메서드
     * @param gameNo 경기 번호
     * @return 경기 정보
     */
    GameVO getGameByNo(@Param("gameNo") int gameNo);

    /**
     * 소모임 번호로 소모임 정보 조회하는 메서드
     * @param meetingNo 소모임 번호
     * @return 소모임 정보
     */
    MeetingVO getMeetingsByMeetingNo(@Param("meetingNo") long meetingNo);

    /**
     * 소모임 번호로 파일 정보 조회하는 메서드
     * @param meetingNo 소모임 번호
     * @return 파일 정보 리스트
     */
    List<MeetingFileVO> getMeetingFileByMeetingNo(@Param("meetingNo") long meetingNo);

    /**
     * 소모임 번호로 참석자 정보 조회하는 메서드
     * @param meetingNo 소모임 번호
     * @return 참석자 정보 리스트
     */
    List<MeetingAttendVO> getMeetingAttendeeByMeetingNo(@Param("meetingNo") long meetingNo);

    /**
     * 모든 경기 날짜를 불러오는 메서드
     * @return 경기 날짜 리스트
     */
    List<String> getAllGameDates();


    // ■■■■■■■■■■■■■■■■■■■ 소모임 기능 (Meeting) ■■■■■■■■■■■■■■■■■■■■ //

    /**
     * 소모임 등록을 위한 메서드
     * @param meetingVO 소모임 정보
     */
    void insertMeeting(MeetingVO meetingVO);

    /**
     * 파일 정보를 삽입하는 메서드
     * @param meetingFileVO 파일 정보
     * @return 삽입된 파일의 수
     */
    int insertMeetingFile(MeetingFileVO meetingFileVO);

    /**
     * 소모임 정보를 수정하는 메서드
     * @param meetingVO 소모임 정보
     */
    void updateMeeting(MeetingVO meetingVO);

    /**
     * 소모임을 삭제하는 메서드
     * @param meetingNo 소모임 번호
     */
    void deleteMeeting(long meetingNo);

    /**
     * 소모임 파일을 삭제하는 메서드
     * @param fileDeleteIds 삭제할 파일 ID 리스트
     */
    void deleteMeetingFiles(@Param("fileDeleteIds") List<Long> fileDeleteIds);


    // ■■■■■■■■■■■■■■■ 소모임 참석/취소 (MeetingAttend) ■■■■■■■■■■■■■■ //

    /**
     * 소모임 참석을 등록하는 메서드
     * @param attendVO 참석 정보
     */
    void insertMeetingAttend(MeetingAttendVO attendVO);

    /**
     * 소모임 참석 여부를 확인하는 메서드
     * @param meetingNo 소모임 번호
     * @param memberId 회원 ID
     * @return 참석 여부 (1: 참석, 0: 참석하지 않음)
     */
    int checkMeetingAttend(@Param("meetingNo") long meetingNo, @Param("memberId") String memberId);

    /**
     * 소모임 참석을 취소하는 메서드
     * @param meetingNo 소모임 번호
     * @param memberId 회원 ID
     */
    void deleteMeetingAttend(@Param("meetingNo") long meetingNo, @Param("memberId") String memberId);
}