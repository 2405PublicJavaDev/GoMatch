package com.moijo.gomatch.app.goods;

import com.moijo.gomatch.domain.goods.service.WishlistService;
import com.moijo.gomatch.domain.member.vo.MemberVO;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/wishlist")
public class WishlistController {

    @Autowired
    private WishlistService wishlistService;

    @PostMapping("/add")
    public String addWishlist(@RequestParam Long goodsNo, HttpSession session, Model model) {
        MemberVO member = (MemberVO) session.getAttribute("member");
        if (member == null) {
            return "redirect:/member/loginpage";  // 로그인 페이지로 리다이렉트
        }

        wishlistService.addWishlist(member.getMemberId(), goodsNo);
        model.addAttribute("message", "상품이 위시리스트에 추가되었습니다.");
        return "wishlist/addSuccess";  // 성공 메시지를 표시할 뷰 페이지
    }

    @PostMapping("/remove")
    public String removeWishlist(@RequestParam Long goodsNo, HttpSession session, Model model) {
        MemberVO member = (MemberVO) session.getAttribute("member");
        if (member == null) {
            return "redirect:/member/loginpage";  // 로그인 페이지로 리다이렉트
        }

        wishlistService.removeWishlist(member.getMemberId(), goodsNo);
        model.addAttribute("message", "상품이 위시리스트에서 삭제되었습니다.");
        return "wishlist/removeSuccess";  // 성공 메시지를 표시할 뷰 페이지
    }
}
