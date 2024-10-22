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
    private int gameNo; // 경기번호
    private Date gameDate; // 경기날짜
    private String gameTime; // 경기시간
    private String teamA; // 홈 팀
    private String teamB; // 원정 팀
    private String regDate; // 생성날짜
    private String gameField; // 경기장소
    private String gameResult; // 경기결과
    private int scoreA; // 홈 팀 점수
    private int scoreB; // 원정 팀 점수
    private String gameStatus;  // 경기비고

    public GameVO(Date sqlDate, String gameTime, String homeTeamName, String awayTeamName, String gameLocation, int homeScore, int awayScore) {
        this.gameDate = sqlDate; 
        this.gameTime = gameTime; 
        this.teamA = homeTeamName; 
        this.teamB = awayTeamName; 
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
