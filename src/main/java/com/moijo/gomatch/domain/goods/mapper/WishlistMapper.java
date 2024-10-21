package com.moijo.gomatch.domain.goods.mapper;

import com.moijo.gomatch.domain.goods.vo.WishlistVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface WishlistMapper {

    // Wishlist에 해당 회원과 상품이 존재하는지 확인하는 쿼리
    @Select("SELECT CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END FROM GOODS_WISHLIST WHERE MEMBER_ID = #{memberId} AND GOODS_NO = #{goodsNo}")
    boolean existsInWishlist(@Param("memberId") String memberId, @Param("goodsNo") Long goodsNo);

    // Wishlist에 상품을 추가하는 쿼리
    @Insert("INSERT INTO GOODS_WISHLIST (MEMBER_ID, GOODS_NO) VALUES (#{memberId}, #{goodsNo})")
    void addToWishlist(@Param("memberId") String memberId, @Param("goodsNo") Long goodsNo);

    // Wishlist에서 상품을 삭제하는 쿼리
    @Delete("DELETE FROM GOODS_WISHLIST WHERE MEMBER_ID = #{memberId} AND GOODS_NO = #{goodsNo}")
    void deleteFromWishlist(@Param("memberId") String memberId, @Param("goodsNo") Long goodsNo);

    @Select("""
    SELECT w.MEMBER_ID, w.GOODS_NO, g.GOODS_PRODUCT_NAME AS GOODS_NAME, g.GOODS_PRICE, 
           gi.GOODS_IMAGE_WEB_PATH, w.REG_DATE
    FROM GOODS_WISHLIST w
    JOIN GOODS g ON w.GOODS_NO = g.GOODS_NO
    LEFT JOIN GOODS_IMAGE gi ON g.GOODS_NO = gi.GOODS_NO AND gi.GOODS_IMAGE_REP_YN = 'Y'
    WHERE w.MEMBER_ID = #{memberId}
""")
    List<WishlistVO> selectWishlistByMemberId(@Param("memberId") String memberId);


}
