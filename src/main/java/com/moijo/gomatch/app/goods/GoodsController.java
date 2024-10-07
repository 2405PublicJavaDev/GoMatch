package com.moijo.gomatch.app.goods;

import com.moijo.gomatch.domain.goods.service.GoodsService;
import com.moijo.gomatch.domain.goods.vo.GoodsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/goods")
public class GoodsController {

    private final GoodsService goodsService;

    @Autowired
    public GoodsController(GoodsService goodsService) {
        this.goodsService = goodsService;
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
    public String getGoodsDetail(@PathVariable Long goodsNo, Model model) {
        GoodsVO goods = goodsService.getGoodsById(goodsNo); // 상품 ID로 상품 조회
        model.addAttribute("goods", goods); // 모델에 추가
        return "goods/detail"; // 상세 조회 템플릿 경로 반환
    }

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
