package com.moijo.gomatch.domain.game.mapper;

import com.moijo.gomatch.domain.game.vo.GameVO;
import org.apache.ibatis.annotations.Mapper;

import java.sql.Date;

@Mapper
public interface GameMapper {
    /**
     * 경기 일정 등록 mapper
     * @param game
     * @return int
     */
    int insertGame(GameVO game);

    /**
     * 경기 점수 업데이트 mapper
     * @param gameDate
     * @param scoreA
     * @param scoreB
     * @return int
     */
    int updateGame(Date gameDate, int scoreA, int scoreB);

    /**
     * 존재하는 경기 조회
     * @param gameDate
     * @param teamA
     * @param teamB
     * @return Integer
     */
    Integer findGameByDateAndTeams(Date gameDate, String teamA, String teamB);
}
