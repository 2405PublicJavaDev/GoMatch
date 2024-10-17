package com.moijo.gomatch.domain.game.mapper;

import com.moijo.gomatch.domain.game.vo.GameVO;
import com.moijo.gomatch.domain.game.vo.StadiumVO;
import org.apache.ibatis.annotations.Mapper;

import java.sql.Date;
import java.util.List;

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
    int updateGame(Date gameDate, int scoreA, int scoreB, String gameStatus);

    /**
     * 존재하는 경기 조회
     * @param gameDate
     * @param teamA
     * @param teamB
     * @return Integer
     */
    Integer findGameByDateAndTeams(Date gameDate, String teamA, String teamB);

    /**
     * 야구장 목록 출력
     * @return List<StadiumVO>
     */
    List<StadiumVO> selectAllStadiums();
}
