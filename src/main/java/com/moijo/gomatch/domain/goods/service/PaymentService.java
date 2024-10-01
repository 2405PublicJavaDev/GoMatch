package com.moijo.gomatch.domain.goods.service;

import com.moijo.gomatch.domain.goods.vo.PaymentRequestVO;
import com.moijo.gomatch.domain.goods.vo.PaymentResponseVO;

public interface PayService {

    String requestPayment(PaymentRequestVO paymentRequestVO);
    PaymentResponseVO verifyPayment(String impUid);


}
