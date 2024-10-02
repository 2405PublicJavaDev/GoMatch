package com.moijo.gomatch.domain.userrank.vo;

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
public class UserRank {
    private Long rankNo;
    private String memberId;
    private Long experiencePoints;
    private String rankPosition;
    private Timestamp rankDate;
    private Timestamp updateDate;
}
