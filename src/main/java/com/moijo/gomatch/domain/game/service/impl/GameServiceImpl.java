package com.moijo.gomatch.domain.game.service.impl;

import com.moijo.gomatch.app.game.GameBatchComponent;
import com.moijo.gomatch.domain.game.mapper.GameMapper;
import com.moijo.gomatch.domain.game.service.GameService;
import com.moijo.gomatch.domain.game.vo.GameVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GameServiceImpl implements GameService {

    private static final Logger logger = LoggerFactory.getLogger(GameBatchComponent.class);

    private GameMapper gameMapper;
    private Map<String, String> teamLogoMap;

    public GameServiceImpl() {
    }

    @Autowired
    public GameServiceImpl(GameMapper gameMapper) {
        this.gameMapper = gameMapper;
        this.teamLogoMap = new HashMap<>();
        this.teamLogoMap.put("KT", "/img/KT로고.png");
        this.teamLogoMap.put("LG", "/img/LG로고.png");
        this.teamLogoMap.put("NC", "/img/NC로고.png");
        this.teamLogoMap.put("SSG", "/img/SSG로고.png");
        this.teamLogoMap.put("KIA", "/img/기아로고.png");
        this.teamLogoMap.put("두산", "/img/두산로고.png");
        this.teamLogoMap.put("롯데", "/img/롯데로고.png");
        this.teamLogoMap.put("삼성", "/img/삼성로고.png");
        this.teamLogoMap.put("키움", "/img/키움로고.png");
        this.teamLogoMap.put("한화", "/img/한화로고.png");
    }

    // 팀 로고 저장
    public String getLogoUrl(String teamName) {
        return teamLogoMap.getOrDefault(teamName, "/img/고매치로고.png");
    }

    // 경기정보 DB에 저장
    @Override
    public void saveAllGames(List<GameVO> gameList) {
        for (GameVO game : gameList) {
            Date currentDate = Date.valueOf(LocalDate.now()); // 현재날짜
            if ((game.getGameDate()).before(currentDate)){
                // 경기날짜가 현재날짜보다 전이면 성적 업데이트
                int result = gameMapper.updateGame(game.getGameDate(), game.getScoreA(), game.getScoreB());
            } else {
                Integer existGameNo = gameMapper.findGameByDateAndTeams(game.getGameDate(), game.getTeamA(), game.getTeamB());
                System.out.println("조회된 경기 번호: " + existGameNo);
                // 경기날짜가 현재날짜보다 같거나 후면 insert (기존데이터 제외)
                if (existGameNo == null) {
                    int result = gameMapper.insertGame(game);
                    logger.info("삽입 결과: {}", result);
                } else {
                    // 이미 존재하는 경우는 insert 하지않음
                    logger.info("경기 정보가 이미 존재합니다: {}", existGameNo);
                }
            }
        }
    }
}
