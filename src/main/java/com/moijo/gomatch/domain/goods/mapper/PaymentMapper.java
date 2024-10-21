package com.moijo.gomatch.domain.goods.mapper;

import com.moijo.gomatch.domain.goods.vo.GoodsPayVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PaymentMapper {
    void insertPaymentInfo(GoodsPayVO goodsPayVO);

    List<GoodsPayVO> selectPaymentsByMemberId(String memberId);

    @Select("SELECT GOODS_NO FROM GOODS WHERE GOODS_PRODUCT_NAME = #{goodsName}")
    Long findGoodsNoByName(String goodsName);
}


