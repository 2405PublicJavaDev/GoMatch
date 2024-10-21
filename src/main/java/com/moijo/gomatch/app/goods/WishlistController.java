package com.moijo.gomatch.app.goods;

import com.moijo.gomatch.domain.goods.service.WishlistService;
import com.moijo.gomatch.domain.goods.vo.WishlistVO;
import com.moijo.gomatch.domain.member.vo.MemberVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/wishlist")
public class WishlistController {

    @Autowired
    private WishlistService wishlistService;

    @GetMapping("/goods/checkLogin")
    @ResponseBody
    public Map<String, Boolean> checkLogin(HttpSession session) {
        Map<String, Boolean> response = new HashMap<>();
        String memberId = (String) session.getAttribute("memberId");
        response.put("loggedIn", memberId != null);
        return response;
    }

    @ModelAttribute
    public void addAttributes(Model model, HttpSession session) {
        MemberVO member = (MemberVO) session.getAttribute("member");
        if (member != null) {
            model.addAttribute("loggedIn", true);
            model.addAttribute("memberNickName", member.getMemberNickName());
        } else {
            model.addAttribute("loggedIn", false);
        }
    }

    @PostMapping("/toggle")
    @ResponseBody // AJAX 요청에 대한 응답을 JSON 형태로 반환
    public ResponseEntity<String> toggleWishlist(@RequestParam("goodsNo") Long goodsNo, HttpSession session) {
        // 세션에서 MemberVO 객체 가져오기
        MemberVO member = (MemberVO) session.getAttribute("member");

        if (member == null) {
            // 로그인하지 않은 사용자
            return ResponseEntity.status(401).body("로그인 필요"); // 로그인 필요 상태 코드 반환
        }

        // 찜하기 로직 처리
        boolean isWishlisted = wishlistService.toggleWishlist(member.getMemberId(), goodsNo);

        // 찜하기 상태에 대한 응답 메시지 반환
        if (isWishlisted) {
            return ResponseEntity.ok("찜하기 추가 성공");
        } else {
            return ResponseEntity.ok("찜하기 제거 성공");
        }
    }

    @GetMapping("/list")
    public String getWishlist(HttpSession session, Model model) {
        MemberVO member = (MemberVO) session.getAttribute("member");

        if (member == null) {
            return "redirect:/member/loginpage"; // 로그인 필요 시 리다이렉트
        }

        List<WishlistVO> wishlist = wishlistService.getWishlistByMemberId(member.getMemberId());
        model.addAttribute("wishlist", wishlist);

        return "goods/wishlist"; // wishlist.html로 이동
    }

}
