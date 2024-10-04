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
    private String gameTime;
    private String teamA;
    private String teamB;
    private String gameResult;   // 경기 결과
    private Integer scoreA;      // 팀1 점수
    private Integer scoreB;      // 팀2 점수
    private String matchPredictDecision;  // 예측 결정
    private String memberId;

    public PredictDTO() {}
}
