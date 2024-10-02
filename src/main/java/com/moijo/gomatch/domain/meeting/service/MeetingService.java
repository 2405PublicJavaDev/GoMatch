package com.moijo.gomatch.domain.meeting.service;

import com.moijo.gomatch.domain.game.vo.GameVO;
import com.moijo.gomatch.domain.meeting.vo.MeetingVO;

import java.util.List;

public interface MeetingService {

    // 날짜에 따른 게임 정보 리스트 불러오기
    List<GameVO> getGamesByDate(String date);
    // 소모임 등록
    void addMeeting(MeetingVO meetingVO);
}
