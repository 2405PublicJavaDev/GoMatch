package com.moijo.gomatch.domain.admin.service.impl;

import com.moijo.gomatch.domain.admin.mapper.AdminMapper1;
import com.moijo.gomatch.domain.admin.service.AdminService1;
import com.moijo.gomatch.domain.admin.vo.AdminVO1;
import com.moijo.gomatch.domain.goods.vo.GoodsVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl1 implements AdminService1 {

    private AdminMapper1 adminMapper1;

    public AdminServiceImpl1(AdminMapper1 adminMapper1) {this.adminMapper1 = adminMapper1;}

    @Override
    public Long insertGoods(AdminVO1 goods) {
        adminMapper1.insertGoods(goods); // 상품 등록
        return goods.getGoodsNo(); // 생성된 goodsNo 반환
    }

    @Override
    public List<GoodsVO> getAllGoods() {
        return adminMapper1.selectGoodsList(); // 전체 상품 목록 조회
    }

    @Override
    public AdminVO1 getGoodsById(Long goodsNo) {
        return adminMapper1.selectGoodsById(goodsNo); // 상품 ID로 조회
    }

    @Override
    public void updateGoods(GoodsVO goods) {
        adminMapper1.updateGoods(goods); // 상품 수정
    }

    @Override
    public void deleteGoods(Long goodsNo) {
        adminMapper1.deleteGoods(goodsNo); // 상품 삭제
    }
}
