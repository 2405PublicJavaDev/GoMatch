package com.moijo.gomatch.domain.goods.vo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class GoodsVO {
    private Long goodsNo;              // 상품 번호
    private String goodsTeam;          // 상품 팀
    private String goodsProductName;   // 상품 이름
    private Integer goodsPrice;        // 상품 가격
    private String goodsProductCode;   // 상품 코드
    private String goodsFrom;          // 상품 출처

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate goodsMakeDate;   // 제조일

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate goodsOutDate;    // 유통기한

    private String goodsCategory;      // 상품 카테고리

    // 추가: 대표 이미지 경로
    private String goodsImageWebPath;  // 대표 이미지 웹 경로
}
