package com.moijo.gomatch.app.goods;

import com.moijo.gomatch.domain.goods.service.WishlistService;
import com.moijo.gomatch.domain.member.vo.MemberVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/wishlist")
public class WishlistController {

    @Autowired
    private WishlistService wishlistService;

    @PostMapping
    public String toggleWishlist(@RequestParam("goodsNo") Long goodsNo, HttpSession session) {
        // 세션에서 MemberVO 객체 가져오기
        MemberVO member = (MemberVO) session.getAttribute("loginUser");

        if (member == null) {
            // 로그인하지 않은 사용자
            return "redirect:/member/loginpage"; // 로그인 페이지로 리디렉션
        }

        // 찜하기 로직 처리
        boolean isWishlisted = wishlistService.toggleWishlist(member.getMemberId(), goodsNo);

        // 찜하기 추가/삭제 후 상세 페이지로 리디렉션하지 않고, 버튼 상태를 클라이언트에서 변경
        return "redirect:/goods/detail/" + goodsNo; // 상세 페이지로 리디렉션
    }
}
