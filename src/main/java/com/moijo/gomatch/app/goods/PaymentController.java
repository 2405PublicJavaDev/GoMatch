package com.moijo.gomatch.app.goods;

import com.moijo.gomatch.domain.goods.service.PaymentService;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/payment")
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/complete")
    @ResponseBody
    public ResponseEntity<String> paymentComplete(@RequestBody Map<String, Object> data, HttpSession session) {
        String impUid = (String) data.get("imp_uid");
        String merchantUid = (String) data.get("merchant_uid");
        String memberId = (String) session.getAttribute("memberId");

        try {
            IamportResponse<Payment> response = paymentService.validateIamport(impUid);
            if (response.getResponse() != null) {
                Payment payment = response.getResponse();
                paymentService.savePaymentInfo(payment, memberId); // MEMBER_ID 포함하여 저장
                return ResponseEntity.ok("결제 성공");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("유효하지 않은 결제 정보");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("결제 실패");
        }
    }
}