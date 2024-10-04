package com.moijo.gomatch.domain.meeting.service;

import com.moijo.gomatch.domain.game.vo.GameVO;
import com.moijo.gomatch.domain.meeting.vo.MeetingVO;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface MeetingService {

    // 날짜에 따른 게임 정보 리스트 불러오기
    List<GameVO> getGamesByDate(String date);
    // 소모임 등록
    void addMeeting(MeetingVO meetingVO);
    // 날짜별 소모임 리스트 불러오기
    List<MeetingVO> getMeetingsByDate(String date);
    List<MeetingVO> getMeetingsByDateAndTeam(String date, String team);
}