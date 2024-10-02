package com.moijo.gomatch.domain.meeting.mapper;


import com.moijo.gomatch.domain.game.vo.GameVO;
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
    // 파일 정보를 삽입하는 메서드 추가
    int insertMeetingFile(MeetingFileVO meetingFileVO);

}
