package com.moijo.gomatch.domain.game.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.N;

@Getter
@Setter
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class StadiumVO {

    private int stadiumId; // 야구장 번호
    private String stadiumName; // 야구장 이름
    private double latitude;    // 위도
    private double longitude;   // 경도
    private String stadiumAddress; // 주소
    private String sidoName; // 시도명

}
