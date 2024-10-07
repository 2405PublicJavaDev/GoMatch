package com.moijo.gomatch.domain.meeting.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class GameDTO {
    private int gameNo;
    private String gameDate;
    private String gameTime;
    private String teamA;
    private String teamB;
    private String gameField;

    // 필요에 따라 필드 추가 가능
}