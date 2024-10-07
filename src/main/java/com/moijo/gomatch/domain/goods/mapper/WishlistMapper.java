package com.moijo.gomatch.domain.goods.mapper;

import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface WishlistMapper {

    void insertWishlist(@Param("memberId") String memberId, @Param("goodsNo") Long goodsNo);

    void deleteWishlist(@Param("memberId") String memberId, @Param("goodsNo") Long goodsNo);

    boolean existsByMemberIdAndGoodsNo(@Param("memberId") String memberId, @Param("goodsNo") Long goodsNo);
}

