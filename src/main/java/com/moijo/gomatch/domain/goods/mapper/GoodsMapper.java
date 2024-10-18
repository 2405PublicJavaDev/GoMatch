package com.moijo.gomatch.domain.goods.mapper;

import com.moijo.gomatch.domain.admin.vo.GoodsImageVO;
import com.moijo.gomatch.domain.goods.vo.GoodsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Mapper
public interface GoodsMapper {

    @Select("SELECT GOODS_NO, GOODS_TEAM, GOODS_PRODUCT_NAME, GOODS_PRICE FROM GOODS") // 필요한 필드만 조회
    List<GoodsVO> selectGoodsList(); // 모든 상품 목록 조회

    @Select("SELECT * FROM GOODS WHERE GOODS_NO = #{goodsNo}")
    GoodsVO selectGoodsById(Long goodsNo); // 상품 조회 메소드 추가

//    List<GoodsVO> selectGoodsByCategory(String category); // 카테고리로 상품 조회 메소드 추가

    List<GoodsVO> selectGoodsByTeam(String team); // 팀으로 상품 조회 메소드 추가

    // 팀별 + 카테고리별 조회 메소드 추가
    List<GoodsVO> selectGoodsByTeamAndCategory(
            @Param("team") String team,
            @Param("category") String category
    );

    List<GoodsVO> searchAllGoods(String searchValue); // 전체 검색 메소드

    List<GoodsVO> searchGoodsByName(String productName); // 이름으로 검색 메소드

    List<GoodsVO> searchGoodsByCode(String goodsProductCode); // 코드로 검색 메소드

    // 추가: 대표 이미지 가져오기
    @Select("SELECT * FROM GOODS_IMAGE WHERE GOODS_NO = #{goodsNo} AND GOODS_IMAGE_REP_YN = 'Y'")
    GoodsImageVO selectRepresentativeImage(Long goodsNo);

    @Select("SELECT * FROM GOODS_IMAGE WHERE GOODS_NO = #{goodsNo}")
    List<GoodsImageVO> selectGoodsImagesByGoodsNo(Long goodsNo);

    @Select("SELECT * FROM GOODS_IMAGE WHERE GOODS_NO = #{goodsNo} AND GOODS_IMAGE_REP_YN = 'N'")
    List<GoodsImageVO> selectDetailImagesByGoodsNo(Long goodsNo);

    @Select("""
        SELECT g.GOODS_NO, g.GOODS_TEAM, g.GOODS_PRODUCT_NAME, g.GOODS_PRICE, 
               gi.GOODS_IMAGE_WEB_PATH AS goodsImageWebPath
        FROM GOODS g
        LEFT JOIN GOODS_IMAGE gi ON g.GOODS_NO = gi.GOODS_NO
        WHERE gi.GOODS_IMAGE_REP_YN = 'Y'
        ORDER BY (
            SELECT COUNT(*) FROM GOODS_WISHLIST w WHERE w.GOODS_NO = g.GOODS_NO
        ) DESC
    """)
    List<GoodsVO> selectGoodsByWishlistCount();

    @Select("""
        SELECT g.GOODS_NO, g.GOODS_TEAM, g.GOODS_PRODUCT_NAME, g.GOODS_PRICE, 
               gi.GOODS_IMAGE_WEB_PATH AS goodsImageWebPath
        FROM GOODS g
        LEFT JOIN GOODS_IMAGE gi ON g.GOODS_NO = gi.GOODS_NO
        WHERE gi.GOODS_IMAGE_REP_YN = 'Y'
        ORDER BY g.GOODS_NO DESC
    """)
    List<GoodsVO> selectNewGoods();

    @Select("SELECT OPTION_NAME FROM GOODS_OPTION WHERE GOODS_NO = #{goodsNo}")
    List<String> selectGoodsOptions(Long goodsNo);  // 옵션 조회 SQL


}
