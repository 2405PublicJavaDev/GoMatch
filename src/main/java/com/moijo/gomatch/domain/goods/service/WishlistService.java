package com.moijo.gomatch.domain.goods.service;

import java.util.List;

public interface WishlistService {
    void addWishlist(String memberId, Long goodsNo);
    void removeWishlist(String memberId, Long goodsNo);
    boolean isWishlistItem(String memberId, Long goodsNo);
}

