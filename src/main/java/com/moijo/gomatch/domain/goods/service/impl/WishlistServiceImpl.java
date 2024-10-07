package com.moijo.gomatch.domain.goods.service.impl;

import com.moijo.gomatch.domain.goods.mapper.WishlistMapper;
import com.moijo.gomatch.domain.goods.service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WishlistServiceImpl implements WishlistService {

    @Autowired
    private WishlistMapper wishlistMapper;

    @Override
    public void addWishlist(String memberId, Long goodsNo) {
        if (!wishlistMapper.existsByMemberIdAndGoodsNo(memberId, goodsNo)) {
            wishlistMapper.insertWishlist(memberId, goodsNo);
        }
    }

    @Override
    public void removeWishlist(String memberId, Long goodsNo) {
        wishlistMapper.deleteWishlist(memberId, goodsNo);
    }

    @Override
    public boolean isWishlistItem(String memberId, Long goodsNo) {
        return wishlistMapper.existsByMemberIdAndGoodsNo(memberId, goodsNo);
    }
}
