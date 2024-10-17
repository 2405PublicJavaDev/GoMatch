package com.moijo.gomatch.domain.goods.mapper;

import org.apache.ibatis.annotations.*;

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
}
