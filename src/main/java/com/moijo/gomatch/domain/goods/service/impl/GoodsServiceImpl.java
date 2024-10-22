package com.moijo.gomatch.domain.goods.service.impl;

import com.moijo.gomatch.domain.admin.vo.GoodsImageVO;
import com.moijo.gomatch.domain.goods.mapper.GoodsMapper;
import com.moijo.gomatch.domain.goods.service.GoodsService;
import com.moijo.gomatch.domain.goods.vo.GoodsVO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

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

    @Override
    public List<String> getGoodsOptions(Long goodsNo) {
        return goodsMapper.selectGoodsOptions(goodsNo);  // 매퍼 호출
    }

    @Override
    public void saveGoodsWithImagesAndOptions(GoodsVO goods, MultipartFile goodsImage,
                                              MultipartFile[] detailImages, List<String> options) throws IOException {
        // 1. 상품 정보 저장 (DB에 등록)
        goodsMapper.insertGoods(goods);
        Long goodsNo = goods.getGoodsNo();  // 등록된 상품 ID 가져오기

        // 2. 옵션 저장
        if (options != null && !options.isEmpty()) {
            for (String option : options) {
                goodsMapper.insertGoodsOption(goodsNo, option);
            }
        }

        // 3. 대표 이미지 저장
        if (!goodsImage.isEmpty()) {
            saveFile(goodsImage, "C:/upload/images/rep/", goodsNo, "REPRESENTATIVE");
        }

        // 4. 상세 이미지 저장
        for (MultipartFile detailImage : detailImages) {
            if (!detailImage.isEmpty()) {
                saveFile(detailImage, "C:/upload/images/det/", goodsNo, "DETAIL");
            }
        }
    }

    private void saveFile(MultipartFile file, String dirPath, Long goodsNo, String type) throws IOException {
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();  // 디렉터리 생성
        }

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        File dest = new File(dirPath + fileName);

        // **원본 파일 그대로 저장**
        file.transferTo(dest);

        // 이미지 정보 DB 저장
        GoodsImageVO goodsImageVO = new GoodsImageVO();
        goodsImageVO.setGoodsNo(goodsNo);
        goodsImageVO.setGoodsImageType(type);
        goodsImageVO.setGoodsImageWebPath(dirPath + fileName);
        goodsImageVO.setGoodsImageRealPath(dest.getAbsolutePath());

        goodsMapper.insertGoodsImage(goodsImageVO);
    }



}
