package com.moijo.gomatch.domain.matchpredict;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.sql.Date;
import java.sql.Timestamp;
@Getter
@Setter
@ToString
@AllArgsConstructor
@Slf4j
public class MatchPredictDTO {
    private Long matchPredictNo;
    private Long gameNo;
    private String matchPredictDecision;
    private String memberId;
    private String teamA;
    private String teamB;
    private String gameTime;
    private Date gameDate;
    private Timestamp regDate;

    public MatchPredictDTO() {

    }
}
