package com.moijo.gomatch.domain.goods.mapper;

import com.moijo.gomatch.domain.goods.vo.GoodsVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

@Mapper
public interface GoodsMapper {

    void insertGoods(GoodsVO goods); // 상품 정보를 추가하는 메소드

    @Select("SELECT GOODS_NO, GOODS_TEAM, GOODS_PRODUCT_NAME, GOODS_PRICE FROM GOODS") // 필요한 필드만 조회
    List<GoodsVO> selectGoodsList(); // 모든 상품 목록 조회

    @Select("SELECT * FROM GOODS WHERE GOODS_NO = #{goodsNo}")
    GoodsVO selectGoodsById(Long goodsNo); // 상품 조회 메소드 추가

    @Update("UPDATE GOODS SET GOODS_TEAM = #{goodsTeam}, GOODS_PRODUCT_NAME = #{goodsProductName}, GOODS_PRICE = #{goodsPrice} WHERE GOODS_NO = #{goodsNo}")
    void updateGoods(GoodsVO goods); // 상품 수정

    @Delete("DELETE FROM GOODS WHERE GOODS_NO = #{goodsNo}")
    void deleteGoods(Long goodsNo); // 상품 삭제
}
