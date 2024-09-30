package com.moijo.gomatch.domain.userrank.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;
@Getter
@Setter
@ToString
public class UserRank {
    private Long rankNo;
    private String memberId;
    private Long experiencePoints;
    private String rankPosition;
    private Timestamp rankDate;
    private Timestamp updateDate;
}
