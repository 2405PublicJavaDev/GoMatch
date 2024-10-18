package com.moijo.gomatch.domain.admin.service;

import com.moijo.gomatch.domain.admin.vo.AdminVO1;
import com.moijo.gomatch.domain.admin.vo.GoodsImageVO;
import com.moijo.gomatch.domain.goods.vo.GoodsVO;

import java.util.List;

public interface AdminService1 {

    Long insertGoods(AdminVO1 goods); // 굿즈 등록 메소드 추가

    List<GoodsVO> getAllGoods(); // 모든 상품 목록을 가져오는 메소드;

    AdminVO1 getGoodsById(Long goodsNo); // 상품 조회 메소드 추가

    void updateGoods(GoodsVO goods); // 상품 수정 메소드 추가

    void deleteGoods(Long goodsNo);

    void insertGoodsImage(GoodsImageVO goodsImage); // 이미지 등록 메소드 추가

    // 추가된 메소드
    List<GoodsImageVO> getGoodsImagesByGoodsNo(Long goodsNo);

    void deleteRepresentativeImage(Long goodsNo);

    void deleteGoodsImage(Long goodsImageNo); // 이미지 삭제 메소드 추가

    void insertGoodsOption(Long goodsNo, String optionName);


}
