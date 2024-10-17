package com.moijo.gomatch.domain.goods.service.impl;

import com.moijo.gomatch.domain.admin.vo.GoodsImageVO;
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
    public List<GoodsVO> getAllGoods() {
        return goodsMapper.selectGoodsList(); // 전체 상품 목록 조회
    }

    @Override
    public GoodsVO getGoodsById(Long goodsNo) {
        return goodsMapper.selectGoodsById(goodsNo); // 상품 ID로 조회
    }

//    @Override
//    public List<GoodsVO> getGoodsByCategory(String category) {
//        return goodsMapper.selectGoodsByCategory(category); // 카테고리로 상품 조회
//    }

    @Override
    public List<GoodsVO> getGoodsByTeam(String team) {
        return goodsMapper.selectGoodsByTeam(team); // 팀으로 상품 조회
    }

    @Override
    public List<GoodsVO> getGoodsByTeamAndCategory(String team, String category) {
        return goodsMapper.selectGoodsByTeamAndCategory(team, category);  // 쿼리 호출
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

    @Override
    public GoodsImageVO getRepresentativeImageByGoodsNo(Long goodsNo) {
        return goodsMapper.selectRepresentativeImage(goodsNo);
    }

//    @Override
//    public List<GoodsImageVO> getGoodsImagesByGoodsNo(Long goodsNo) {
//        return goodsMapper.selectGoodsImagesByGoodsNo(goodsNo);
//    }

    @Override
    public List<GoodsImageVO> getDetailImagesByGoodsNo(Long goodsNo) {
        return goodsMapper.selectDetailImagesByGoodsNo(goodsNo);
    }

    @Override
    public List<GoodsVO> getGoodsByWishlistCount() {
        return goodsMapper.selectGoodsByWishlistCount();
    }

    @Override
    public List<GoodsVO> getNewGoods() {
        return goodsMapper.selectNewGoods();
    }


}
