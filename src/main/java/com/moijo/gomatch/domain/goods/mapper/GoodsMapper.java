package com.moijo.gomatch.domain.goods.mapper;

import com.moijo.gomatch.domain.goods.vo.GoodsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface GoodsMapper {

    @Select("SELECT GOODS_NO, GOODS_TEAM, GOODS_PRODUCT_NAME, GOODS_PRICE FROM GOODS") // 필요한 필드만 조회
    List<GoodsVO> selectGoodsList(); // 모든 상품 목록 조회

    @Select("SELECT * FROM GOODS WHERE GOODS_NO = #{goodsNo}")
    GoodsVO selectGoodsById(Long goodsNo); // 상품 조회 메소드 추가

    List<GoodsVO> selectGoodsByCategory(String category); // 카테고리로 상품 조회 메소드 추가

    List<GoodsVO> searchAllGoods(String searchValue); // 전체 검색 메소드

    List<GoodsVO> searchGoodsByName(String productName); // 이름으로 검색 메소드

    List<GoodsVO> searchGoodsByCode(String goodsProductCode); // 코드로 검색 메소드

}
