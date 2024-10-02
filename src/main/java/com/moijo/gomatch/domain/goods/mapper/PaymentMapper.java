package com.moijo.gomatch.domain.goods.mapper;

import com.moijo.gomatch.domain.goods.vo.PaymentRequestVO;
import com.moijo.gomatch.domain.goods.vo.PaymentResponseVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PaymentMapper {
    void insertPayment(PaymentRequestVO paymentRequestVO);
    PaymentResponseVO getPaymentById(String impUid); // 필요에 따라 구현 및 사용 검토
}
