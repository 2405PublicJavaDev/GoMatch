package com.moijo.gomatch.domain.goods.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentResponseVO {

    private String status;
    private String imp_uid;
    private String merchant_uid;
    private String pay_method;
}
