package com.moijo.gomatch.domain.goods.mapper;

import com.moijo.gomatch.domain.goods.vo.PaymentRequestVO;
import com.moijo.gomatch.domain.goods.vo.PaymentResponseVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PayMapper {
    void insertPayment(PaymentRequestVO paymentRequestVO);
    PaymentResponseVO getPaymentById(String impUid);
}
