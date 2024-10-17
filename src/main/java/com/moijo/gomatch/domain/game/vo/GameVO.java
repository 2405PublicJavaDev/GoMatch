package com.moijo.gomatch.domain.game.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.sql.Date;

@Getter
@Setter
@Slf4j
@NoArgsConstructor
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
    private String gameStatus;  // 경기비고

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
                "gameNo=" + gameNo +
                ", gameDate=" + gameDate +
                ", gameTime='" + gameTime + '\'' +
                ", teamA='" + teamA + '\'' +
                ", teamB='" + teamB + '\'' +
                ", regDate='" + regDate + '\'' +
                ", gameField='" + gameField + '\'' +
                ", memberId='" + memberId + '\'' +
                ", gameResult='" + gameResult + '\'' +
                ", scoreA=" + scoreA +
                ", scoreB=" + scoreB +
                ", gameStatus='" + gameStatus + '\'' +
                '}';
    }

    // MyBatis에서 자동으로 매핑할 수 있는 생성자 추가(윤경)
    public GameVO(int gameNo, String gameDate, String gameTime, String teamA, String teamB, String gameField) {
        this.gameNo = gameNo;
        this.gameDate = Date.valueOf(gameDate);
        this.gameTime = gameTime;
        this.teamA = teamA;
        this.teamB = teamB;
        this.gameField = gameField;
    }


}
