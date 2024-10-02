package com.moijo.gomatch.domain.goods.service.impl;

import com.moijo.gomatch.domain.goods.mapper.PaymentMapper;
import com.moijo.gomatch.domain.goods.service.PaymentService;
import com.moijo.gomatch.domain.goods.service.PortOne; // PortOne 클래스 경로 확인 필요
import com.moijo.gomatch.domain.goods.vo.PaymentRequestVO;
import com.moijo.gomatch.domain.goods.vo.PaymentResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentMapper paymentMapper;

    @Override
    public String requestPayment(PaymentRequestVO paymentRequestVO) {
        // PortOne API 호출 예시
        PortOne portOne = new PortOne("7551064815828466", "DwosLgyUNz0MWjA8SE7hfSwYTEEmbNLEEmQfLmpDHqZvT8jmxeczats4I65Xu2i01aZEToL7LT8hUi9e"); // 실제 키로 수정

        // 결제 요청 로직
        Map<String, String> paymentParams = new HashMap<>();
        paymentParams.put("amount", paymentRequestVO.getAmount());
        paymentParams.put("buyer_name", paymentRequestVO.getMemberId()); // 구매자 이름은 memberId로 변경
        paymentParams.put("buyer_email", paymentRequestVO.getBuyerEmail()); // buyerEmail 필드를 추가해야 합니다.
        paymentParams.put("buyer_phone", paymentRequestVO.getGoodsPayPhone()); // 전화번호로 변경
        paymentParams.put("product_name", "상품명"); // 상품명은 적절히 설정해야 합니다.

        // PortOne API를 통해 결제 요청
        String impUid = portOne.requestPayment(paymentParams);

        // DB에 결제 정보 저장
        paymentMapper.insertPayment(paymentRequestVO);

        return impUid;
    }

    @Override
    public PaymentResponseVO verifyPayment(String impUid) {
        // 결제 확인 로직
        PortOne portOne = new PortOne("your_api_key", "your_api_secret"); // 실제 키로 수정
        PaymentResponseVO paymentInfo = portOne.getPaymentData(impUid);

        return paymentInfo;
    }
}
