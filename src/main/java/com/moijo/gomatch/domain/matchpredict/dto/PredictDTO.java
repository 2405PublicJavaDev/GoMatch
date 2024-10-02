package com.moijo.gomatch.domain.matchpredict.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.sql.Date;

@Getter
@Setter
@ToString
@AllArgsConstructor
@Slf4j
public class PredictDTO {
    private Long gameNo;
    private Date gameDate;
    private String gameTitle;
    private String teamA;
    private String teamB;
    private String gameResult;
    private String matchPredictDecision;
}
