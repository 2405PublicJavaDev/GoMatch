package com.moijo.gomatch.domain.admin.mapper;

import com.moijo.gomatch.domain.admin.vo.AdminVO1;
import com.moijo.gomatch.domain.admin.vo.GoodsImageVO;
import com.moijo.gomatch.domain.goods.vo.GoodsVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface AdminMapper1 {

    void insertGoods(AdminVO1 goods); // 상품 정보를 추가하는 메소드

    @Select("SELECT GOODS_NO, GOODS_TEAM, GOODS_PRODUCT_NAME, GOODS_PRICE FROM GOODS") // 필요한 필드만 조회
    List<GoodsVO> selectGoodsList(); // 모든 상품 목록 조회

    @Select("SELECT * FROM GOODS WHERE GOODS_NO = #{goodsNo}")
    AdminVO1 selectGoodsById(Long goodsNo); // 상품 조회 메소드 추가

    @Update("UPDATE GOODS SET GOODS_TEAM = #{goodsTeam}, GOODS_PRODUCT_NAME = #{goodsProductName}, GOODS_PRICE = #{goodsPrice}, GOODS_CATEGORY = #{goodsCategory} WHERE GOODS_NO = #{goodsNo}")
    void updateGoods(GoodsVO goods); // 상품 수정

    @Delete("DELETE FROM GOODS WHERE GOODS_NO = #{goodsNo}")
    void deleteGoods(Long goodsNo); // 상품 삭제

    void insertGoodsImage(GoodsImageVO goodsImage); // 이미지 정보를 추가하는 메소드

}
