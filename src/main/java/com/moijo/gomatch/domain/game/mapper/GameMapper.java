package com.moijo.gomatch.domain.game.mapper;

import com.moijo.gomatch.domain.game.vo.GameVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GameMapper {
    /**
     * 경기 일정 등록 mapper
     * @param game
     * @return int
     */
    int insertGame(GameVO game);

    /**
     * 경기 존재 확인 mapper
     * @param gameDate
     * @param teamA
     * @param teamB
     * @return
     */
    boolean existsByDateAndTeams(String gameDate, String teamA, String teamB);
}
