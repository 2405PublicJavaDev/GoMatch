package com.moijo.gomatch.domain.goods.service;

import com.moijo.gomatch.domain.goods.vo.PaymentResponseVO;

import java.util.Map;

public class PortOne {
    private String apiKey;
    private String apiSecret;

    public PortOne(String apiKey, String apiSecret) {
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
    }

    public String requestPayment(Map<String, String> paymentParams) {
        // 결제 요청 로직 구현
        return "임시 결제 ID"; // 실제 결제 요청 후 반환되는 값으로 수정
    }

    public PaymentResponseVO getPaymentData(String impUid) {
        // 결제 데이터 확인 로직 구현
        return new PaymentResponseVO(); // 실제 반환값으로 수정
    }
}
