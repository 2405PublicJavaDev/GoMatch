package com.moijo.gomatch.domain.game.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.sql.Date;

@Getter
@Setter
@Slf4j
@AllArgsConstructor
public class GameVO {
    private int gameNo;
    private Date gameDate;
    private String gameTime;
    private String teamA;
    private String teamB;
    private String regDate;
    private String gameField;
    private String memberId;
    private String gameResult;
    private int scoreA;
    private int scoreB;

    public GameVO(Date sqlDate, String gameTime, String homeTeamName, String awayTeamName, String gameLocation, int homeScore, int awayScore) {
        this.gameDate = sqlDate;
        this.gameTime = gameTime;
        this.teamA = homeTeamName; // 홈 팀
        this.teamB = awayTeamName; // 원정 팀
        this.gameField = gameLocation;
        this.scoreA = homeScore;
        this.scoreB = awayScore;
    }

    @Override
    public String toString() {
        return "GameVO{" +
                "date='" + gameDate + '\'' +
                ", time='" + gameTime + '\'' +
                ", homeTeam='" + teamA + '\'' +
                ", awayTeam='" + teamB + '\'' +
                ", location='" + gameField + '\'' +
                ", homeScore=" + scoreA +
                ", awayScore=" + scoreB +
                '}';
    }


    // MyBatis에서 자동으로 매핑할 수 있는 생성자 추가(윤경)
    public GameVO(int gameNo, String gameDate, String gameTime, String teamA, String teamB, String gameField) {
        this.gameNo = gameNo;
        this.gameDate = gameDate;
        this.gameTime = gameTime;
        this.teamA = teamA;
        this.teamB = teamB;
        this.gameField = gameField;
    }


}
