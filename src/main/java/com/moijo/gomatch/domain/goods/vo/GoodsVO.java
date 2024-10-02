package com.moijo.gomatch.domain.goods.vo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
public class GoodsVO {
    private Long goodsNo; // 상품 번호
    private String goodsTeam; // 상품 팀
    private String goodsProductName; // 상품 이름
    private Integer goodsPrice; // 상품 가격
    private String goodsProductCode; // 상품 코드
    private String goodsFrom; // 상품 출처

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime goodsMakeDate; // 제조일

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime goodsOutDate; // 유통기한
}
