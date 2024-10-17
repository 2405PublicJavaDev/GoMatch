//package com.moijo.gomatch.app.goods;
//
//import com.moijo.gomatch.domain.goods.service.PaymentService;
//import com.siot.IamportRestClient.IamportClient;
//import com.siot.IamportRestClient.exception.IamportResponseException;
//import com.siot.IamportRestClient.response.IamportResponse;
//import com.siot.IamportRestClient.response.Payment;
//import jakarta.servlet.http.HttpSession;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//
//import java.io.IOException;
//import java.util.Map;
//
//@Controller
//public class PaymentController {
//
//    @Autowired
//    private PaymentService paymentService;
//    private final IamportClient iamportClient;
//
//    @Autowired
//    private ReservationService rService;
//    @Value("${IMP_API_KEY}") // application.properties 에 값 작성 ex ) IMP_API_KEY = 123446252352
//    String apiKey;
//    @Value("${IMP_API_SECRETKEY}") //API_KEY 랑 동일하게 작성
//    String apiSecret;
//
//    public PaymentController(PaymentService paymentService) {
//        this.paymentService = paymentService;
//        this.iamportClient = new IamportClient(apiKey, apiSecret);
//    }
//
//    //결제 준비 매핑 별 다른 html 파일 없음 결제로 오기 전 가져와야하는 데이터 가져오기 위해 POST 매핑용 메소드
//    @PostMapping("/paymentReady")
//    public String readyTogoPay(@ModelAttribute("ReservationDTO") ReservationDTO rDTO,
//                               @ModelAttribute("ShowInfoDTO") ShowInfoDTO sDTO, Model model, HttpSession session) {
//        System.out.println("paymentReady" + rDTO);
//        String memberId = (String) session.getAttribute("memberId");
//        MemberDTO info = rService.selectMemberInfo(memberId);
//        System.out.println("info" + info);
//
//        rDTO.setBuyer_email(info.getEmail());
//        rDTO.setBuyer_name(info.getName());
//        rDTO.setBuyer_tel(info.getPhone());
//
//        session.setAttribute("rDTO", rDTO);
//        session.setAttribute("sDTO", sDTO);
//        System.out.println("rDTO 함 보여바라" + rDTO);
//        System.out.println("sDTO Payment" + sDTO);
//        model.addAttribute("rDTO", rDTO);
//        return "redirect:/payment?reservationNo=" + rDTO.getReservationNo();
//    }
//
//    // readyTogoPay 메소드 값 가지고 html 파일 가는 메소드
//    @GetMapping("/payment")
//    public String showPayForm(@ModelAttribute ReservationDTO rDTO,
//                              @ModelAttribute("ShowInfoDTO") ShowInfoDTO sDTO, Model model, HttpSession session) {
//        String memberId = (String) session.getAttribute("memberId");
//        ReservationDTO DTO = (ReservationDTO) session.getAttribute("rDTO");
//        ShowInfoDTO DTOs = (ShowInfoDTO) session.getAttribute("sDTO");
//        model.addAttribute("memberId", memberId);
//        System.out.println("showPayForm" + DTO);
//        System.out.println("showPayForm sDTO" + sDTO);
//        model.addAttribute("rDTO", DTO);
//        model.addAttribute("sDTO", DTOs);
//        return "pages/payment/inipay";
//    }
//
//    // 결제 성공 후 결제 한 제품에 대한 정보 조회하는 메소드
//    @PostMapping("/validation/{imp_uid}")
//    @ResponseBody
//    public IamportResponse<Payment> validateIamport(@PathVariable String imp_uid)
//            throws IamportResponseException, IOException {
//        System.out.println("validation Controller :" + imp_uid);
//        return paymentService.validateIamport(imp_uid);
//    }
//
//    // 정보 조회한 결제 제품 저장하는 메소드
//    @PostMapping("/save_buyerInfo")
//    @ResponseBody
//    public ResponseEntity<String> saveBuyerInfo(
//            @RequestBody Map<String, Object> data
//            , HttpSession session
//    ) {
//        Map<String, Object> buyerInfo = (Map<String, Object>) data.get("buyerInfo");
//        Map<String, Object> reserveInfo = (Map<String, Object>) data.get("reserveInfo");
//
//        System.out.println("saveBuyerInfo" + buyerInfo);
//        System.out.println("saveBuyerInfo2" + reserveInfo);
//        String memberId = (String) session.getAttribute("memberId");
//
//        paymentService.saveBuyerAndOrderInfo(buyerInfo, reserveInfo);
//        return ResponseEntity.ok("결제정보 저장 완");
//    }
//    // 결제 취소 하는 메소드
//    @GetMapping("/payments/cancel/{imp_uid}")
//    public ResponseEntity<String> cancelPayment(@PathVariable String imp_uid) {
//        System.out.println("Cancel: " + imp_uid);
//        IamportResponse<Payment> response = paymentService.cancelPayment(imp_uid);
//        paymentService.deleteReserveAndPaymentInfo(imp_uid);
//        return ResponseEntity.ok("취소 완!");
//    }
//    // 결제 취소 하기 위해 필요한 imp_Uid 라는 키 얻는 메소드
//    @GetMapping("/getImpUid")
//    public ResponseEntity<String> getImpUid(@RequestParam String reservationNo) {
//        System.out.println("getReservationNo: " + reservationNo);
//        String impUid = paymentService.selectImpUid(reservationNo);
//
//        System.out.println("impUid : " + impUid);
//        return ResponseEntity.ok(impUid);
//    }
//}
//
