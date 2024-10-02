package com.moijo.gomatch.domain.game.service.impl;

import com.moijo.gomatch.domain.game.mapper.GameMapper;
import com.moijo.gomatch.domain.game.service.GameService;
import com.moijo.gomatch.domain.game.vo.GameVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Service
public class GameServiceImpl implements GameService {

    private GameMapper gameMapper;

    public GameServiceImpl() {

    }

    @Autowired
    public GameServiceImpl(GameMapper gameMapper) {
        this.gameMapper = gameMapper;
    }

    @Override
    public void saveAllGames(List<GameVO> gameList) {
        for (GameVO game : gameList) {
            Date currentDate = Date.valueOf(LocalDate.now()); // 현재날짜
            if ((game.getGameDate()).before(currentDate)){
                // 경기날짜가 현재날짜보다 전이면 성적 업데이트
                int result = gameMapper.updateGame(game.getGameDate(), game.getScoreA(), game.getScoreB());
            } else {
                Integer existGameNo = gameMapper.findGameByDateAndTeams(game.getGameDate(), game.getTeamA(), game.getTeamB());
                // 경기날짜가 현재날짜보다 같거나 후면 insert (기존데이터 제외)
                if (existGameNo == null) {
                    int result = gameMapper.insertGame(game);
                } else {
                    // 이미 존재하는 경우는 insert 하지않음
                    System.out.println("경기 정보가 이미 존재합니다 : " + existGameNo);
                }
            }
        }
    }
}
