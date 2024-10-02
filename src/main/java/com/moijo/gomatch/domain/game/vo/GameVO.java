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
    private String gameDate;
    private String gameTime;
    private String teamA;
    private String teamB;
    private String regDate;
    private String gameField;
    private String memberId;
    private String gameResult;
    private int scoreA;
    private int scoreB;

    // SQL Date를 인자로 받는 생성자 구현
    public GameVO(Date sqlDate, String gameTime, String homeTeamName, String awayTeamName, String gameLocation, int homeScore, int awayScore) {
        this.gameDate = sqlDate.toString(); // SQL Date를 String으로 변환하여 저장
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
}
