package com.moijo.gomatch.domain.game.service.impl;

import com.moijo.gomatch.domain.game.mapper.GameMapper;
import com.moijo.gomatch.domain.game.service.GameService;
import com.moijo.gomatch.domain.game.vo.GameVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
            if(gameMapper.existsByDateAndTeams(game.getGameDate(), game.getTeamA(), game.getTeamB())){
//                if((game.getScoreA()) & game.getScoreB()).equals(null))
//                int result = gameMapper.updateGame(game);
            }

            // 기존에 있던 경기 제외하고 DB저장
            if (!gameMapper.existsByDateAndTeams(game.getGameDate(), game.getTeamA(), game.getTeamB())) {
                int result = gameMapper.insertGame(game);
            }
        }
    }
}
