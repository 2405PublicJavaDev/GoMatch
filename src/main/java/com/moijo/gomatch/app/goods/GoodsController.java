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

    // 굿즈 등록 폼을 보여주는 메소드
    @GetMapping("/insert")
    public String showRegisterForm() {
        return "goods/insert"; // 등록 폼의 템플릿 경로
    }

    @PostMapping("/insert")
    public String registerGoods(GoodsVO goods, Model model) {
        goodsService.insertGoods(goods); // 굿즈 등록 서비스 호출
        model.addAttribute("message", "상품이 성공적으로 등록되었습니다.");
        return "redirect:/goods/list"; // 등록 후 상품 목록으로 리다이렉트
    }

    @GetMapping("/list")
    public String getGoodsList(Model model) {
        List<GoodsVO> goodsList = goodsService.getAllGoods(); // 서비스 메소드를 호출하여 상품 목록을 가져옴
        model.addAttribute("goodsList", goodsList); // 모델에 추가
        return "goods/list"; // Thymeleaf 템플릿 경로 반환
    }

//    @GetMapping("/category/list")
//    public String getGoodsListByCategory(@RequestParam("category") String category, Model model) {
//        List<GoodsVO> goodsList = goodsService.getGoodsByCategory(category); // 카테고리로 상품 조회
//        model.addAttribute("goodsList", goodsList); // 모델에 추가
//        return "goods/categorylist"; // 카테고리 리스트를 보여줄 템플릿 경로
//    }

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

    // 수정 폼을 보여주는 메소드 (상세 페이지에서 수정)
    @GetMapping("/edit/{goodsNo}")
    public String showEditForm(@PathVariable Long goodsNo, Model model) {
        GoodsVO goods = goodsService.getGoodsById(goodsNo); // 상품 ID로 상품 조회
        model.addAttribute("goods", goods); // 모델에 추가
        return "goods/edit"; // 수정 폼 템플릿 경로 반환
    }

    @PostMapping("/update")
    public String updateGoods(@ModelAttribute GoodsVO goods, Model model) {
        goodsService.updateGoods(goods); // 서비스 메소드를 호출하여 상품 수정
        model.addAttribute("message", "상품이 성공적으로 수정되었습니다.");
        return "redirect:/goods/list"; // 수정 후 상품 목록으로 리다이렉트
    }

    @PostMapping("/delete") // 삭제 요청을 처리하는 메소드
    public String deleteGoods(@RequestParam("goodsNo") Long goodsNo, Model model) {
        goodsService.deleteGoods(goodsNo); // 서비스 메소드를 호출하여 상품 삭제
        model.addAttribute("message", "상품이 성공적으로 삭제되었습니다.");
        return "redirect:/goods/list"; // 삭제 후 상품 목록으로 리다이렉트
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
