package com.moijo.gomatch.domain.goods.service;

public interface WishlistService {

    boolean toggleWishlist(String memberId, Long goodsNo);

    boolean isWishlisted(String memberId, Long goodsNo);
}

