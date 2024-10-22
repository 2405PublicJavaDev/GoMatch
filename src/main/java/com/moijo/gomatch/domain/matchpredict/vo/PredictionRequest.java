package com.moijo.gomatch.domain.matchpredict.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@ToString
@AllArgsConstructor
@Slf4j
public class PredictionRequest {
    private Long gameNo;
    private Long matchPredictNo;
    private String matchPredictDecision;
}
