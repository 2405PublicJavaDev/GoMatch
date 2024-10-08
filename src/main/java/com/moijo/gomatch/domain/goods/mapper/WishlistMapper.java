package com.moijo.gomatch.domain.goods.mapper;

import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface WishlistMapper {

    // 기존의 쿼리를 수정합니다.
    @Select("SELECT CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END FROM GOODS_WISHLIST WHERE MEMBER_ID = ? AND GOODS_NO = ?")
    boolean existsInWishlist(@Param("memberId") String memberId, @Param("goodsNo") Long goodsNo);

    @Insert("INSERT INTO GOODS_WISHLIST (MEMBER_ID, GOODS_NO) VALUES (#{memberId}, #{goodsNo})")
    void addToWishlist(@Param("memberId") String memberId, @Param("goodsNo") Long goodsNo);

    @Delete("DELETE FROM GOODS_WISHLIST WHERE MEMBER_ID = #{memberId} AND GOODS_NO = #{goodsNo}")
    void deleteFromWishlist(@Param("memberId") String memberId, @Param("goodsNo") Long goodsNo);
}


