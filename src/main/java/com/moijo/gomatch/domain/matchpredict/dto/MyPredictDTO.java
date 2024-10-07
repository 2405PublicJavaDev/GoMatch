package com.moijo.gomatch.domain.matchpredict.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;
@Getter
@Setter
@ToString
@AllArgsConstructor
@Slf4j
public class MyPredictDTO {
    private Long gameNo;
    private Timestamp gameDate;
    private String gameTime;
    private String teamA;
    private String teamB;
    private String matchPredictDecision;  // 예측 결정
    private String memberId;
}
