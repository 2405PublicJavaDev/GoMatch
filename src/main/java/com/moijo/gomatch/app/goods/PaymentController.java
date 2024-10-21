package com.moijo.gomatch.app.goods;

import com.moijo.gomatch.domain.goods.service.PaymentService;
import com.moijo.gomatch.domain.goods.vo.GoodsPayVO;
import com.moijo.gomatch.domain.member.vo.MemberVO;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/payment")
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

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

    // 마이페이지에서 세션에 저장된 memberId로 구매 목록 조회
    @GetMapping("/list")
    public String getPurchaseList(HttpSession session, Model model) {
        String memberId = (String) session.getAttribute("memberId");

        if (memberId == null) {
            // 세션에 memberId가 없을 경우, 로그인 페이지로 리다이렉트
            return "redirect:/login";
        }

        // memberId로 구매 목록 조회
        List<GoodsPayVO> purchaseList = paymentService.getPaymentListByMemberId(memberId);
        model.addAttribute("purchaseList", purchaseList);

        // 디버깅 로그 추가
        System.out.println("memberId: " + memberId);
        System.out.println("조회된 구매 내역: " + purchaseList);

        return "goods/purchaseList"; // purchaseList.html로 이동
    }

    @PostMapping("/complete")
    @ResponseBody
    public ResponseEntity<String> paymentComplete(@RequestBody Map<String, Object> data, HttpSession session) {
        String impUid = (String) data.get("imp_uid");
        String merchantUid = (String) data.get("merchant_uid");
        String memberId = (String) session.getAttribute("memberId");

        // 디버깅 로그
        System.out.println("impUid: " + impUid);
        System.out.println("merchantUid: " + merchantUid);
        System.out.println("memberId: " + memberId);

        try {
            IamportResponse<Payment> response = paymentService.validateIamport(impUid);
            if (response != null && response.getResponse() != null) {
                Payment payment = response.getResponse();

                Long goodsNo = paymentService.getGoodsNoByMerchantUid(merchantUid);
                if (goodsNo == null) {
                    System.out.println("유효하지 않은 상품 번호");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("유효하지 않은 상품 정보");
                }

                paymentService.savePaymentInfo(payment, memberId, goodsNo);
                return ResponseEntity.ok("결제 성공");
            } else {
                System.out.println("유효하지 않은 결제 정보");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("유효하지 않은 결제 정보");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("결제 실패");
        }
    }


}
