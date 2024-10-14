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
public class UserRankDTO {
    private Long rankNo;
    private String memberId;
    private String experiencePoints;
    private Timestamp regDate;
    private Timestamp updateDate;
}
