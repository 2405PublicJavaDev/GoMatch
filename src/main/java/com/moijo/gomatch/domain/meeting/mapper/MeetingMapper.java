package com.moijo.gomatch.domain.meeting.mapper;


import com.moijo.gomatch.domain.game.vo.GameVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface MeetingMapper {
    // 날짜별 경기를 조회하는 메서드
    List<GameVO> getGamesByDate(@Param("date") LocalDate date);
}
