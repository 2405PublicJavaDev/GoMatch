package com.moijo.gomatch.domain.goods.vo;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class GoodsPayVO {
    private Long goodsPayNo;
    private Long goodsNo;
    private String memberId;
    private String goodsPayArrival;
    private String goodsRequirement;
    private String goodsPayAddressCode;
    private String goodsPayPhone;
    private String goodsPayReturnAccount;
    private LocalDateTime regDate;
    private LocalDateTime updateDate;

    // 포맷된 날짜를 저장할 필드
    private String formattedRegDate;
}
