package com.moijo.gomatch.domain.meeting.service;

import com.moijo.gomatch.domain.game.vo.GameVO;
import com.moijo.gomatch.domain.meeting.dto.GameDTO;

import java.time.LocalDate;
import java.util.List;

public interface MeetingService {

    List<GameVO> getGamesByDate(LocalDate date);
}
