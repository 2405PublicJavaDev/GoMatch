package com.moijo.gomatch.domain.goods.service;

import com.moijo.gomatch.domain.goods.vo.WishlistVO;

import java.util.List;

public interface WishlistService {

    boolean toggleWishlist(String memberId, Long goodsNo);

    boolean isWishlisted(String memberId, Long goodsNo);

    List<WishlistVO> getWishlistByMemberId(String memberId); // 위시리스트 조회
}

