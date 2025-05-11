package com.study.ecommerce.controller;

import com.study.ecommerce.request.OrderRequest;
import com.study.ecommerce.response.kakao.KakaoPayApproveResponse;
import com.study.ecommerce.response.kakao.KakaoPayResponse;
import com.study.ecommerce.service.KakaoPayService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/payment")
public class PaymentController {

    private final KakaoPayService kakaoPayService;


    @PostMapping("/ready")
    @ResponseBody
    public KakaoPayResponse kakaoPay(@RequestBody OrderRequest orderRequest, HttpSession httpSession) {
        String partnerOrderId = UUID.randomUUID().toString();
        KakaoPayResponse kakaoPayResponse = kakaoPayService.kakaoPayReady(orderRequest,partnerOrderId);

        httpSession.setAttribute("tid",kakaoPayResponse.getTid());
        httpSession.setAttribute("partnerOrderId",partnerOrderId);
        httpSession.setAttribute("orderRequest",orderRequest);


        return kakaoPayResponse;
    }

    // 주의! 결제하고 그 다음 창에서 넘어갈 때 jwt토큰이 없어서 되돌아 올때 로그인이 필요한 걸로 간주! 그렇기 때문에 이 부분 해결 생각!
    @GetMapping("/success")
    public ResponseEntity<KakaoPayApproveResponse> kakaoPaySuccess(@RequestParam("pg_token") String pgToken,
                                                  HttpSession httpSession) throws IllegalAccessException {

        String tid = (String) httpSession.getAttribute("tid");
        String partnerOrderId = (String) httpSession.getAttribute("partnerOrderId");
        OrderRequest orderRequest = (OrderRequest) httpSession.getAttribute("orderRequest");

        // 결제 승인 요청
        KakaoPayApproveResponse approveResponse = kakaoPayService.approve(
                pgToken, tid, partnerOrderId, String.valueOf(orderRequest.getMemberId())
        );

        // 세션 정리
        httpSession.removeAttribute("tid");
        httpSession.removeAttribute("partnerOrderId");
        httpSession.removeAttribute("orderRequest");


        return ResponseEntity.ok(approveResponse);
    }

    @GetMapping("/cancel")
    public String kakaoCancel() {
        return "결제가 취소되었습니다.";
    }

    @GetMapping("/fail")
    public String kakaoFail() {
        return "결제에 실패했습니다.";
    }


}
