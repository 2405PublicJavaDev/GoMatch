package com.moijo.gomatch.domain.matchpredict.dto;

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
public class MemberRankDTO {
    private Long memberId;
    private String memberName;
    private String memberNickname;
    private Long experiencePoints;
    private Long rankPosition;
}
