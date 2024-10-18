package com.moijo.gomatch.domain.goods.mapper;

import com.moijo.gomatch.domain.goods.vo.GoodsPayVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PaymentMapper {
    void insertPaymentInfo(GoodsPayVO goodsPayVO);
}


