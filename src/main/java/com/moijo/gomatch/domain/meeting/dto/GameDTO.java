package com.moijo.gomatch.domain.meeting.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GameDTO {
    private int gameNo;      // 경기 번호
    private String gameDate; // 경기 날짜
    private String gameTime; // 경기 시간
    private String teamA;    // A팀
    private String teamB;    // B팀
    private String gameField; // 경기 장소
}