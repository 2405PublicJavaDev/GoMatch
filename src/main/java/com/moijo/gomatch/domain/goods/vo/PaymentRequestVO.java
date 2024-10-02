package com.moijo.gomatch.domain.goods.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRequestVO {

    private Long goodsPayNo; // GOODS_PAY_NO
    private Long goodsNo; // GOODS_NO
    private String memberId; // MEMBER_ID
    private String goodsPayArrival; // GOODS_PAY_ARRIVAL
    private String goodsRequirement; // GOODS_REQUIREMENT
    private String goodsPayAddressCode; // GOODS_PAY_ADDRESS_CODE
    private String goodsPayPhone; // GOODS_PAY_PHONE
    private String goodsPayReturnAccount; // GOODS_PAY_RETURN_ACCOUNT
    private String amount; // 결제 금액
    private String buyerEmail; // 구매자 이메일 추가
}
