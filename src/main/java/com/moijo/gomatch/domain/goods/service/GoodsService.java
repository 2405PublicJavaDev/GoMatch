package com.moijo.gomatch.domain.goods.service;

import com.moijo.gomatch.domain.admin.vo.GoodsImageVO;
import com.moijo.gomatch.domain.goods.vo.GoodsVO;

import java.util.List;

public interface GoodsService {

    List<GoodsVO> getAllGoods(); // 모든 상품 목록을 가져오는 메소드;

    GoodsVO getGoodsById(Long goodsNo); // 상품 조회 메소드 추가

//    List<GoodsVO> getGoodsByCategory(String category); // 카테고리로 상품 조회 메소드 추가

    List<GoodsVO> getGoodsByTeamAndCategory(String team, String category);  // 팀 + 카테고리 조회 추가

    List<GoodsVO> getGoodsByTeam(String team); // 팀으로 상품 조회 메소드 추가

    List<GoodsVO> searchAllGoods(String searchValue); // 전체 검색 메소드 추가

    List<GoodsVO> searchGoodsByName(String productName); // 이름으로 검색 메소드 추가

    List<GoodsVO> searchGoodsByCode(String goodsProductCode); // 코드로 검색 메소드 추가

    List<GoodsImageVO> getDetailImagesByGoodsNo(Long goodsNo);

    List<GoodsVO> getGoodsByWishlistCount();  // 찜하기 많은 순 조회

    List<GoodsVO> getNewGoods();  // 최신 등록 순 조회

    GoodsImageVO getRepresentativeImageByGoodsNo(Long goodsNo);  // 대표 이미지 조회

    List<String> getGoodsOptions(Long goodsNo);  // 옵션 조회 메서드



}

