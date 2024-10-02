package com.moijo.gomatch.domain.goods.service.impl;

import com.moijo.gomatch.domain.goods.mapper.GoodsMapper;
import com.moijo.gomatch.domain.goods.service.GoodsService;
import com.moijo.gomatch.domain.goods.vo.GoodsVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsServiceImpl implements GoodsService {

    private final GoodsMapper goodsMapper;

    public GoodsServiceImpl(GoodsMapper goodsMapper) {
        this.goodsMapper = goodsMapper;
    }

    @Override
    public void insertGoods(GoodsVO goods) {
        goodsMapper.insertGoods(goods); // 상품 등록
    }

    @Override
    public List<GoodsVO> getAllGoods() {
        return goodsMapper.selectGoodsList(); // 전체 상품 목록 조회
    }

    @Override
    public GoodsVO getGoodsById(Long goodsNo) {
        return goodsMapper.selectGoodsById(goodsNo); // 상품 ID로 조회
    }

    @Override
    public List<GoodsVO> getGoodsByCategory(String category) {
        return goodsMapper.selectGoodsByCategory(category); // 카테고리로 상품 조회
    }

    @Override
    public void updateGoods(GoodsVO goods) {
        goodsMapper.updateGoods(goods); // 상품 수정
    }

    @Override
    public void deleteGoods(Long goodsNo) {
        goodsMapper.deleteGoods(goodsNo); // 상품 삭제
    }

    @Override
    public List<GoodsVO> searchAllGoods(String searchValue) {
        return goodsMapper.searchAllGoods(searchValue);
    }

    @Override
    public List<GoodsVO> searchGoodsByName(String productName) {
        return goodsMapper.searchGoodsByName(productName);
    }

    @Override
    public List<GoodsVO> searchGoodsByCode(String goodsProductCode) {
        return goodsMapper.searchGoodsByCode(goodsProductCode);
    }
}
