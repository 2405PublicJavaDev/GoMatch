package com.moijo.gomatch.app.goods;

import com.moijo.gomatch.domain.admin.vo.GoodsImageVO;
import com.moijo.gomatch.domain.goods.service.GoodsService;
import com.moijo.gomatch.domain.goods.service.WishlistService;
import com.moijo.gomatch.domain.goods.vo.GoodsVO;
import com.moijo.gomatch.domain.member.vo.MemberVO;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/goods")
public class GoodsController {

    private final GoodsService goodsService;
    private final WishlistService wishlistService; // 필드 선언

    @Autowired
    public GoodsController(GoodsService goodsService, WishlistService wishlistService) {
        this.goodsService = goodsService;
        this.wishlistService = wishlistService;
    }

    @GetMapping("/main")
    public String getGoodsList(Model model) {
        // 찜하기 많은 순 상품 목록 (최대 10개)
        List<GoodsVO> popularGoods = goodsService.getGoodsByWishlistCount();
        for (GoodsVO goods : popularGoods) {
            GoodsImageVO representativeImage = goodsService.getRepresentativeImageByGoodsNo(goods.getGoodsNo());
            if (representativeImage != null) {
                goods.setGoodsImageWebPath(representativeImage.getGoodsImageWebPath());
            }
        }
        // 상위 10개만 남기기
        if (popularGoods.size() > 10) {
            popularGoods = popularGoods.subList(0, 10);
        }

        // 최신 등록 상품 목록 (최대 10개)
        List<GoodsVO> newGoods = goodsService.getNewGoods();
        for (GoodsVO goods : newGoods) {
            GoodsImageVO representativeImage = goodsService.getRepresentativeImageByGoodsNo(goods.getGoodsNo());
            if (representativeImage != null) {
                goods.setGoodsImageWebPath(representativeImage.getGoodsImageWebPath());
            }
        }
        // 상위 10개만 남기기
        if (newGoods.size() > 10) {
            newGoods = newGoods.subList(0, 10);
        }

        model.addAttribute("popularGoods", popularGoods);  // 찜하기 많은 순 목록 전달
        model.addAttribute("newGoods", newGoods);          // 최신 등록 목록 전달

        return "goods/main";  // 하나의 HTML 페이지에서 두 목록 출력
    }

    @GetMapping("/team/list")
    public String getGoodsListByTeam(@RequestParam("team") String team, Model model) {
        List<GoodsVO> goodsList = goodsService.getGoodsByTeam(team);
        model.addAttribute("goodsList", goodsList);
        return "goods/teamlist";
    }

    // 팀별 + 카테고리별 상품 조회
    @GetMapping("/team/category/list")
    public String getGoodsByTeamAndCategory(
            @RequestParam("team") String team,
            @RequestParam("category") String category,
            Model model) {
        List<GoodsVO> goodsList = goodsService.getGoodsByTeamAndCategory(team, category);
        model.addAttribute("team", team);  // 팀 이름 전달
        model.addAttribute("category", category);  // 카테고리 전달
        model.addAttribute("goodsList", goodsList);
        return "goods/teamlist";
    }

    @GetMapping("/detail/{goodsNo}")
    public String getGoodsDetail(@PathVariable Long goodsNo, HttpSession session, Model model) {
        GoodsVO goods = goodsService.getGoodsById(goodsNo); // 상품 ID로 상품 세부 정보 조회
        model.addAttribute("goods", goods);

        // 대표 이미지 조회
        GoodsImageVO representativeImage = goodsService.getRepresentativeImageByGoodsNo(goodsNo);
        model.addAttribute("representativeImage", representativeImage);

        // 상세 이미지 조회
        List<GoodsImageVO> detailImages = goodsService.getDetailImagesByGoodsNo(goodsNo);
        model.addAttribute("detailImages", detailImages);

        List<String> options = goodsService.getGoodsOptions(goodsNo);  // 옵션 조회
        model.addAttribute("options", options);

        // 로그인한 사용자 정보 가져오기
        MemberVO member = (MemberVO) session.getAttribute("member");

        if (member != null) {
            // 찜하기 상태 체크
            boolean isWishlisted = wishlistService.isWishlisted(member.getMemberId(), goodsNo);
            model.addAttribute("isWishlisted", isWishlisted);
        } else {
            model.addAttribute("isWishlisted", false);
        }

        return "goods/detail";
    }

    @GetMapping("/search")
    public String searchGoods(@RequestParam("searchValue") String searchValue,
                              @RequestParam("searchType") String searchType,
                              Model model) {
        List<GoodsVO> goodsList;

        // 검색 유형에 따라 상품 조회
        if ("all".equals(searchType)) {
            goodsList = goodsService.searchAllGoods(searchValue);
        } else if ("name".equals(searchType)) {
            goodsList = goodsService.searchGoodsByName(searchValue);
        } else {
            goodsList = goodsService.searchGoodsByCode(searchValue);
        }

        // 각 상품의 대표 이미지 설정
        for (GoodsVO goods : goodsList) {
            GoodsImageVO representativeImage = goodsService.getRepresentativeImageByGoodsNo(goods.getGoodsNo());
            if (representativeImage != null) {
                goods.setGoodsImageWebPath(representativeImage.getGoodsImageWebPath());
            }
        }

        model.addAttribute("goodsList", goodsList);
        return "goods/search"; // 검색 결과 페이지로 이동
    }

    @GetMapping("/payment/{goodsNo}")
    public String showPaymentForm(@PathVariable Long goodsNo, HttpSession session, Model model) {
        GoodsVO goods = goodsService.getGoodsById(goodsNo);
        MemberVO member = (MemberVO) session.getAttribute("member");

        if (member == null) {
            return "redirect:/member/loginpage";  // 비로그인 시 로그인 페이지로 이동
        }

        model.addAttribute("goods", goods);
        model.addAttribute("member", member);
        return "goods/paymentForm";  // 결제 정보 입력 페이지로 이동
    }

}