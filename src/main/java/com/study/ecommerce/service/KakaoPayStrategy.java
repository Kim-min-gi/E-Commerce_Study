package com.study.ecommerce.service;

import com.study.ecommerce.config.KakaoPayProperties;
import com.study.ecommerce.domain.type.Payment;
import com.study.ecommerce.request.OrderRequest;
import com.study.ecommerce.request.PaymentRequest;
import com.study.ecommerce.response.kakao.KakaoPayApproveResponse;
import com.study.ecommerce.response.kakao.KakaoPayCancelResponse;
import com.study.ecommerce.response.kakao.KakaoPayResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoPayStrategy implements PaymentStrategy {

    private final KakaoPayProperties kakaoPayProperties;



    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "SECRET_KEY " + kakaoPayProperties.getAdminKey()); // 카카오 관리자 키
        headers.set("Content-type", "application/json");

        return headers;
    }


    public KakaoPayResponse ready(PaymentRequest paymentRequest) {

        RestTemplate restTemplate = new RestTemplate();
        String partnerOrderId = UUID.randomUUID().toString();


        Map<String, String> params = new HashMap<>();
        params.put("cid", "TC0ONETIME"); // 테스트 CID
        params.put("partner_order_id", partnerOrderId);
        params.put("partner_user_id", String.valueOf(paymentRequest.getOrderRequest().getMemberId()));
        params.put("item_name", paymentRequest.getOrderRequest().getItemName());
        params.put("quantity", String.valueOf(paymentRequest.getOrderRequest().getOrderItemRequestList().size()));
        params.put("total_amount", String.valueOf(paymentRequest.getOrderRequest().getTotalPrice()));
        params.put("tax_free_amount", "0");
        params.put("approval_url", "http://localhost:8080/payment/success");
        params.put("cancel_url", "http://localhost:8080/payment/cancel");
        params.put("fail_url", "http://localhost:8080/payment/fail");
        HttpEntity<Map<String, String>> body = new HttpEntity<>(params, this.getHeaders());
        ResponseEntity<KakaoPayResponse> kakaoPayResponseResponseEntity = restTemplate.postForEntity(
                "https://open-api.kakaopay.com/online/v1/payment/ready",
                body,
                KakaoPayResponse.class
        );
        KakaoPayResponse response = kakaoPayResponseResponseEntity.getBody();
        Objects.requireNonNull(response).setPartner_order_id(partnerOrderId);

        return response;

    }


    public KakaoPayApproveResponse approve(PaymentRequest paymentRequest) throws IllegalAccessException {
        RestTemplate restTemplate = new RestTemplate();

        if (paymentRequest.getTid() == null || paymentRequest.getPartnerOrderId() == null) {
            throw new IllegalAccessException("잘못된 접근입니다.");
        }


        Map<String, String> params = new HashMap<>();
        params.put("cid", "TC0ONETIME");
        params.put("tid", paymentRequest.getTid());
        params.put("partner_order_id", paymentRequest.getPartnerOrderId());
        params.put("partner_user_id", paymentRequest.getPartnerUserId());
        params.put("pg_token", paymentRequest.getPgToken());

        HttpEntity<Map<String, String>> request = new HttpEntity<>(params, this.getHeaders());

        return restTemplate.postForObject(
                "https://open-api.kakaopay.com/online/v1/payment/approve",
                request,
                KakaoPayApproveResponse.class
        );
    }

    public KakaoPayCancelResponse cancel(PaymentRequest paymentRequest) throws IllegalAccessException {
        RestTemplate restTemplate = new RestTemplate();

        if (paymentRequest.getTid() == null || paymentRequest.getPartnerOrderId() == null) {
            throw new IllegalAccessException("잘못된 접근입니다.");
        }

        Map<String, String> params = new HashMap<>();
        params.put("cid", "TC0ONETIME");
        params.put("tid", paymentRequest.getTid());
        params.put("cancel_amount",String.valueOf(paymentRequest.getOrderRequest().getTotalPrice()));
        params.put("cancel_tax_free_amount", "0");

        HttpEntity<Map<String, String>> request = new HttpEntity<>(params, this.getHeaders());

        return restTemplate.postForObject(
                "https://open-api.kakaopay.com/online/v1/payment/cancel",
                request,
                KakaoPayCancelResponse.class
        );
    }

    @Override
    public Payment getPaymentType() {
        return Payment.KAKAO_PAY;
    }


}
