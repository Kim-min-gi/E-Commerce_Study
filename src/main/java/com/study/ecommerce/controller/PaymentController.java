package com.study.ecommerce.controller;

import com.study.ecommerce.request.OrderRequest;
import com.study.ecommerce.request.PaymentRequest;
import com.study.ecommerce.response.PaymentResponse;
import com.study.ecommerce.service.KakaoPayStrategy;
import com.study.ecommerce.service.PaymentService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/payment")
public class PaymentController {

    private final KakaoPayStrategy kakaoPayStrategy;

    private final PaymentService paymentService;


    @PostMapping("/ready")
    @ResponseBody
    public ResponseEntity<PaymentResponse> ready(@RequestBody PaymentRequest paymentRequest, HttpSession httpSession) {

        PaymentResponse paymentResponse = paymentService.ready(paymentRequest);

        httpSession.setAttribute("tid",paymentResponse.getTid());
        httpSession.setAttribute("partnerOrderId",paymentResponse.getPartner_order_id());
        httpSession.setAttribute("orderRequest",paymentRequest.getOrderRequest());


        return ResponseEntity.ok(paymentResponse);
    }

    // 주의! 결제하고 그 다음 창에서 넘어갈 때 jwt토큰이 없어서 되돌아 올때 로그인이 필요한 걸로 간주! 그렇기 때문에 이 부분 해결 생각!
    @GetMapping("/success")
    public ResponseEntity<PaymentResponse> success(@RequestParam("pg_token") String pgToken,
                                                  HttpSession httpSession) throws IllegalAccessException {

        String tid = (String) httpSession.getAttribute("tid");
        String partnerOrderId = (String) httpSession.getAttribute("partnerOrderId");
        OrderRequest orderRequest = (OrderRequest) httpSession.getAttribute("orderRequest");

        PaymentRequest paymentRequest = PaymentRequest.builder()
                .tid(tid)
                .orderRequest(orderRequest)
                .partnerOrderId(partnerOrderId)
                .pgToken(pgToken)
                .partnerUserId(String.valueOf(orderRequest.getMemberId()))
                .build();


        PaymentResponse paymentResponse = paymentService.approve(paymentRequest);

        // 세션 정리
        httpSession.removeAttribute("tid");
        httpSession.removeAttribute("partnerOrderId");
        httpSession.removeAttribute("orderRequest");


        return ResponseEntity.ok(paymentResponse);
    }

    @GetMapping("/cancel")
    public ResponseEntity<PaymentResponse> cancel(@RequestBody PaymentRequest paymentRequest) throws IllegalAccessException {

        PaymentResponse paymentResponse = paymentService.cancel(paymentRequest);

        return ResponseEntity.ok(paymentResponse);
    }

    @GetMapping("/fail")
    public String fail() {
        return "결제에 실패했습니다.";
    }


}
