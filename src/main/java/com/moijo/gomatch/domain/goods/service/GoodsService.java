package com.moijo.gomatch.domain.goods.service;

import com.moijo.gomatch.domain.goods.vo.GoodsVO;

import java.util.List;

public interface GoodsService {

    void insertGoods(GoodsVO goods); // 굿즈 등록 메소드 추가

    List<GoodsVO> getAllGoods(); // 모든 상품 목록을 가져오는 메소드;

    GoodsVO getGoodsById(Long goodsNo); // 상품 조회 메소드 추가

    List<GoodsVO> getGoodsByCategory(String category); // 카테고리로 상품 조회 메소드 추가

    void updateGoods(GoodsVO goods); // 상품 수정 메소드 추가

    void deleteGoods(Long goodsNo);

    List<GoodsVO> searchAllGoods(String searchValue); // 전체 검색 메소드 추가

    List<GoodsVO> searchGoodsByName(String productName); // 이름으로 검색 메소드 추가

    List<GoodsVO> searchGoodsByCode(String goodsProductCode); // 코드로 검색 메소드 추가
}
