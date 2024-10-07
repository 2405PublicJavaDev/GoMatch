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
public class MemberDTO {
    private String memberId;
    private String memberNickname;
    private String memberName;
    private Long experiencePoints;
    private Long rankPosition;
}
