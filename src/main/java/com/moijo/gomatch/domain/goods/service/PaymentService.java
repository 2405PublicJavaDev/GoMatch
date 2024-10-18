package com.moijo.gomatch.domain.goods.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moijo.gomatch.domain.goods.mapper.PaymentMapper;
import com.moijo.gomatch.domain.goods.vo.GoodsPayVO;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentService {

    private final PaymentMapper paymentMapper;
    private final IamportClient iamportClient;

    @Autowired
    public PaymentService(PaymentMapper paymentMapper,
                          @Value("${IMP_API_KEY}") String apiKey,
                          @Value("${IMP_API_SECRETKEY}") String apiSecret) {
        this.paymentMapper = paymentMapper;
        this.iamportClient = new IamportClient(apiKey, apiSecret);
    }

    public IamportResponse<Payment> validateIamport(String impUid) throws IOException {
        try {
            return iamportClient.paymentByImpUid(impUid);
        } catch (IamportResponseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void savePaymentInfo(Payment payment, String memberId) {
        GoodsPayVO goodsPayVO = new GoodsPayVO();

        // 필수 값 설정
        goodsPayVO.setGoodsNo(Long.parseLong(payment.getMerchantUid().split("_")[1]));
        goodsPayVO.setMemberId(memberId);  // 세션에서 전달된 memberId 사용
        goodsPayVO.setGoodsPayPhone(payment.getBuyerTel());
        goodsPayVO.setGoodsPayArrival(payment.getBuyerAddr());
        goodsPayVO.setGoodsPayAddressCode(payment.getBuyerPostcode());

        // 커스텀 데이터 파싱 (JSON 형식)
        Map<String, String> customDataMap = parseCustomData(payment.getCustomData());

        // 요청 메시지 및 환불 계좌 설정
        goodsPayVO.setGoodsRequirement(customDataMap.getOrDefault("requirement", "요청사항 없음"));
        goodsPayVO.setGoodsPayReturnAccount(customDataMap.getOrDefault("returnAccount", "없음"));

        // 데이터베이스에 결제 정보 저장
        paymentMapper.insertPaymentInfo(goodsPayVO);
    }

    // 커스텀 데이터 파싱 메서드
    private Map<String, String> parseCustomData(String customData) {
        if (customData == null || customData.isEmpty()) {
            return new HashMap<>(); // 비어있을 경우 빈 맵 반환
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(customData, Map.class);
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<>(); // 파싱 실패 시 빈 맵 반환
        }
    }
}
