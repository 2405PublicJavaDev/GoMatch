package com.moijo.gomatch.domain.matchpredict.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;
@Getter
@Setter
@ToString
public class MatchPredict {
    private Long mpNo;
    private String mpDecision;
    private String memberId;
    private Long gameNo;
    private String mpStatus;
    private Timestamp regDate;
    private Timestamp updateDate;
}
