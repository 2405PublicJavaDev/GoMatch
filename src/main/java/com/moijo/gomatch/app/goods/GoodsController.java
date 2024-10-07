package com.moijo.gomatch.app.goods;

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

    @GetMapping("/list")
    public String getGoodsList(Model model) {
        List<GoodsVO> goodsList = goodsService.getAllGoods(); // 서비스 메소드를 호출하여 상품 목록을 가져옴
        model.addAttribute("goodsList", goodsList); // 모델에 추가
        return "goods/list"; // Thymeleaf 템플릿 경로 반환
    }

    @GetMapping("/category/list")
    public String getGoodsListByCategory(@RequestParam("category") String category, Model model) {
        List<GoodsVO> goodsList = goodsService.getGoodsByCategory(category);
        model.addAttribute("goodsList", goodsList);
        return "goods/categorylist";
    }

    @GetMapping("/detail/{goodsNo}")
    public String getGoodsDetail(@PathVariable Long goodsNo, HttpSession session, Model model) {
        GoodsVO goods = goodsService.getGoodsById(goodsNo); // 상품 ID로 상품 조회
        model.addAttribute("goods", goods); // 모델에 추가

        // 로그인한 사용자 정보 가져오기
        MemberVO member = (MemberVO) session.getAttribute("member");
        boolean isWishlist = false;
        boolean isLoggedIn = (member != null); // 로그인 상태 확인

        // 사용자가 로그인한 상태일 때만 위시리스트 상태 확인
        if (isLoggedIn) {
            isWishlist = wishlistService.isWishlistItem(member.getMemberId(), goodsNo);
        }

        model.addAttribute("isWishlist", isWishlist); // 위시리스트 상태 추가
        model.addAttribute("isLoggedIn", isLoggedIn); // 로그인 상태 추가

        return "goods/detail"; // 상세 조회 템플릿 경로 반환
    }


//    @GetMapping("/detail/{goodsNo}")
//    public String getGoodsDetail(@PathVariable Long goodsNo, Model model) {
//        GoodsVO goods = goodsService.getGoodsById(goodsNo); // 상품 ID로 상품 조회
//        model.addAttribute("goods", goods); // 모델에 추가
//        return "goods/detail"; // 상세 조회 템플릿 경로 반환
//    }

    @GetMapping("/search")
    public String searchGoods(@RequestParam("searchValue") String searchValue,
                              @RequestParam("searchType") String searchType,
                              Model model) {
        List<GoodsVO> goodsList;

        if ("all".equals(searchType)) {
            goodsList = goodsService.searchAllGoods(searchValue);
        } else if ("name".equals(searchType)) {
            goodsList = goodsService.searchGoodsByName(searchValue);
        } else {
            goodsList = goodsService.searchGoodsByCode(searchValue);
        }

        model.addAttribute("goodsList", goodsList);
        return "goods/list"; // 검색 후 상품 목록으로 이동
    }
}
