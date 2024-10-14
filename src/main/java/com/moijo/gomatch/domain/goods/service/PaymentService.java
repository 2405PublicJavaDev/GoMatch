//package com.moijo.gomatch.domain.goods.service;
//
//import com.moijo.gomatch.domain.goods.mapper.PaymentMapper;
//import com.siot.IamportRestClient.IamportClient;
//import com.siot.IamportRestClient.request.CancelData;
//import com.siot.IamportRestClient.response.IamportResponse;
//import com.siot.IamportRestClient.response.Payment;
//import jakarta.annotation.PostConstruct;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.Map;
//
//
//@Configuration
//@Service
//public class PaymentService {
//
//    private IamportClient iamportClient;
//
//    @Value("${IMP_API_KEY}")
//    String apiKey;
//    @Value("${IMP_API_SECRETKEY}")
//    String apiSecret;
//
//    @PostConstruct
//    public void init() {
//        this.iamportClient = new IamportClient(apiKey, apiSecret);
//    }
//
//    @Autowired
//    private PaymentMapper pmapper;
//
//    @Autowired
//    private ReservationMapper rmapper;
//    // 제품 확인 메소드
//    public IamportResponse<Payment> validateIamport(String imp_uid) {
//        try {
//            IamportResponse<Payment> payment = iamportClient.paymentByImpUid(imp_uid);
//            System.out.println("Service ValidatepaymentInfo:" + payment);
//            return payment;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//    // 결제 취소 메소드
//    public IamportResponse<Payment> cancelPayment(String imp_uid) {
//        try {
//            CancelData cancelData = new CancelData(imp_uid, true);
//            IamportResponse<Payment> payment = iamportClient.cancelPaymentByImpUid(cancelData);
//            System.out.println("Service CancelpaymentInfo:" + payment);
//
//            return payment;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//    // 결제 정보 와 주문 정보 저장 메소드
//    public void saveBuyerAndOrderInfo(Map<String, Object> buyerInfo, Map<String, Object> reserveInfo) {
//        rmapper.insertReservationInfo(reserveInfo);
//        pmapper.insertPaymentInfo(buyerInfo);
//    }
//    // imp_uid 조회 메소드
//    public String selectImpUid(String reservationNo) {
//        return pmapper.selectImpUid(reservationNo);
//    }
//    // 결제 취소 후 결제 db에 저장된 정보와 작성자 기준 예매 정보 삭제 메소드
//    @Transactional
//    public void deleteReserveAndPaymentInfo(String impUid) {
//        rmapper.deleteReservationInfo(impUid);
//        pmapper.deletePaymentInfo(impUid);
//    }
//}
//
