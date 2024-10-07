package com.moijo.gomatch.domain.game.service;

import com.moijo.gomatch.domain.game.vo.GameVO;

import java.util.List;

public interface GameService {
    /**
     * DB에 경기들 저장
     * @param gameList
     */
    void saveAllGames(List<GameVO> gameList);

    /**
     * 팀 로고 이미지 저장
     * @param teamName
     * @return string
     */
    String getLogoUrl(String teamName);

}
