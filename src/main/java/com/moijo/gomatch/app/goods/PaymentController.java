package com.moijo.gomatch.app.goods;

import com.moijo.gomatch.domain.goods.service.PaymentService;
import com.moijo.gomatch.domain.goods.vo.PaymentRequestVO;
import com.moijo.gomatch.domain.goods.vo.PaymentResponseVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/payment")
@Slf4j
@RequiredArgsConstructor // Lombok을 사용하여 생성자 주입
public class PaymentController {

    private final PaymentService paymentService;

    // 결제 페이지로 이동
    @GetMapping("/form")
    public String paymentForm() {
        return "payment/payment"; // payment.html 파일 경로
    }

    // 결제 요청 처리
    @PostMapping("/request")
    public String requestPayment(@ModelAttribute PaymentRequestVO paymentRequestVO, Model model) {
        try {
            // 결제 요청 처리
            String impUid = paymentService.requestPayment(paymentRequestVO);
            model.addAttribute("impUid", impUid);
            return "payment/paymentRequestSuccess"; // 결제 요청 성공 페이지
        } catch (Exception e) {
            log.error("결제 요청 중 오류 발생: {}", e.getMessage());
            model.addAttribute("error", "결제 요청에 실패했습니다: " + e.getMessage());
            return "payment/paymentRequestFail"; // 결제 요청 실패 페이지
        }
    }

    // 결제 성공 처리
    @PostMapping("/complete")
    public String completePayment(@RequestParam String impUid, Model model) {
        // impUid를 사용하여 결제 검증 로직
        PaymentResponseVO paymentInfo = paymentService.verifyPayment(impUid);

        if ("paid".equals(paymentInfo.getStatus())) {
            model.addAttribute("message", "결제가 성공적으로 완료되었습니다.");
            return "payment/paymentSuccess"; // 결제 성공 페이지
        } else {
            model.addAttribute("message", "결제가 실패했습니다.");
            return "payment/paymentFail"; // 결제 실패 페이지
        }
    }
}
