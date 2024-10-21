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
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
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

    public List<GoodsPayVO> getPaymentListByMemberId(String memberId) {
        return paymentMapper.selectPaymentsByMemberId(memberId);
    }

    public IamportResponse<Payment> validateIamport(String impUid) throws IOException {
        try {
            return iamportClient.paymentByImpUid(impUid);
        } catch (IamportResponseException e) {
            e.printStackTrace();
            return null;
        }
    }

    // 상품 번호를 merchantUid에서 추출하는 메서드
    public Long getGoodsNoByMerchantUid(String merchantUid) {
        try {
            // merchantUid에서 상품 번호 추출 (예: "order_123" -> 123)
            String[] parts = merchantUid.split("_");
            if (parts.length < 2) {
                return null;
            }
            return Long.parseLong(parts[1]);  // 상품 번호 추출
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Transactional
    public void savePaymentInfo(Payment payment, String memberId, Long goodsNo) {
        GoodsPayVO goodsPayVO = new GoodsPayVO();

        // 결제 정보 매핑
        goodsPayVO.setGoodsNo(goodsNo);
        goodsPayVO.setMemberId(memberId);
        goodsPayVO.setGoodsPayPhone(payment.getBuyerTel());
        goodsPayVO.setGoodsPayArrival(payment.getBuyerAddr());
        goodsPayVO.setGoodsPayAddressCode(payment.getBuyerPostcode());

        Map<String, String> customDataMap = parseCustomData(payment.getCustomData());
        goodsPayVO.setGoodsRequirement(customDataMap.getOrDefault("requirement", "요청사항 없음"));
        goodsPayVO.setGoodsPayReturnAccount(customDataMap.getOrDefault("returnAccount", "없음"));

        paymentMapper.insertPaymentInfo(goodsPayVO);
    }

    private Map<String, String> parseCustomData(String customData) {
        if (customData == null || customData.isEmpty()) {
            return new HashMap<>();
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(customData, Map.class);
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }
}
