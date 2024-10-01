package com.moijo.gomatch.domain.goods.service.impl;

import org.springframework.stereotype.Service;

@Service
public class PayServiceImpl {

    @Autowired
    private PaymentMapper paymentMapper;

    @Override
    public String requestPayment(PaymentRequestVO paymentRequestVO) {
        // PortOne API 호출 예시
        PortOne portOne = new PortOne("your_api_key", "your_api_secret");

        // 결제 요청 로직
        // 이니시스 결제 프로세스를 PortOne으로 처리
        Map<String, String> paymentParams = new HashMap<>();
        paymentParams.put("amount", paymentRequestVO.getAmount());
        paymentParams.put("buyer_name", paymentRequestVO.getBuyerName());
        paymentParams.put("buyer_email", paymentRequestVO.getBuyerEmail());
        paymentParams.put("buyer_phone", paymentRequestVO.getBuyerPhone());
        paymentParams.put("product_name", paymentRequestVO.getProductName());

        // PortOne API를 통해 결제 요청
        String impUid = portOne.requestPayment(paymentParams);

        // DB에 결제 정보 저장
        paymentMapper.insertPayment(paymentRequestVO);

        return impUid;
    }

    @Override
    public PaymentResponseVO verifyPayment(String impUid) {
        // 결제 확인 로직
        PortOne portOne = new PortOne("your_api_key", "your_api_secret");
        PaymentResponseVO paymentInfo = portOne.getPaymentData(impUid);

        return paymentInfo;
    }
}
