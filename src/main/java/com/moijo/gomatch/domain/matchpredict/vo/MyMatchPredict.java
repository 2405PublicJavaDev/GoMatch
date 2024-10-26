package com.moijo.gomatch.domain.matchpredict.vo;

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
public class MyMatchPredict {
    private Long matchPredictNo;
    private Long gameNo;
    private String matchPredictDecision;
    private String memberId;
    private String matchPredictStatus;
    private String gameTime;
    private Timestamp regDate;
    private Timestamp updateDate;
}
