package com.moijo.gomatch.domain.game.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

@Getter
@Setter
@Slf4j
@AllArgsConstructor
public class GameVO {
    private int gameNo;
    private String gameDate;
    private String gameTime;
    private String teamA;
    private String vs;
    private String teamB;
    private String gameField;

    public GameVO(String gameDate, String gameTime,String teamA,  String vs, String teamB, String gameField) {
        this.gameDate = gameDate;
        this.gameTime = gameTime;
        this.teamA = teamA;
        this.vs = vs;
        this.teamB = teamB;
        this.gameField = gameField;
    }
}
