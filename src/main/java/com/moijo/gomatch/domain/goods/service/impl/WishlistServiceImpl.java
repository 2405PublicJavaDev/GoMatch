package com.moijo.gomatch.domain.goods.service.impl;

import com.moijo.gomatch.domain.goods.mapper.WishlistMapper;
import com.moijo.gomatch.domain.goods.service.WishlistService;
import com.moijo.gomatch.domain.goods.vo.WishlistVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WishlistServiceImpl implements WishlistService {

    @Autowired
    private WishlistMapper wishlistMapper;

    @Override
    public boolean toggleWishlist(String memberId, Long goodsNo) {
        // memberId와 goodsNo를 로그로 출력
        System.out.println("Toggling wishlist for member: " + memberId + ", goodsNo: " + goodsNo);
        System.out.println("memberId: " + memberId + ", goodsNo: " + goodsNo); // 여기 추가

        // Wishlist에 이미 존재하는지 확인
        boolean exists = wishlistMapper.existsInWishlist(memberId, goodsNo);

        if (exists) {
            // 존재하면 삭제
            wishlistMapper.deleteFromWishlist(memberId, goodsNo);
            return false; // 찜 해제 상태 반환
        } else {
            // 존재하지 않으면 추가
            wishlistMapper.addToWishlist(memberId, goodsNo);
            return true; // 찜 상태 반환
        }
    }

    @Override
    public boolean isWishlisted(String memberId, Long goodsNo) {
        return wishlistMapper.existsInWishlist(memberId, goodsNo); // 찜하기 상태 반환
    }

    @Override
    public List<WishlistVO> getWishlistByMemberId(String memberId) {
        return wishlistMapper.selectWishlistByMemberId(memberId);
    }
}
