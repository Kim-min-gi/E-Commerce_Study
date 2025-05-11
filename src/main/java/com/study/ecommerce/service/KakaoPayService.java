package com.study.ecommerce.service;

import com.study.ecommerce.request.OrderRequest;
import com.study.ecommerce.response.kakao.KakaoPayApproveResponse;
import com.study.ecommerce.response.kakao.KakaoPayResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoPayService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${kakao.adminKey}")
    private String kakaoAdminKey;


    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "SECRET_KEY " + kakaoAdminKey); // 카카오 관리자 키
        headers.set("Content-type", "application/json");

        return headers;
    }


    public KakaoPayResponse kakaoPayReady(OrderRequest orderRequest, String partnerOrderId) {

        Map<String, String> params = new HashMap<>();
        params.put("cid", "TC0ONETIME"); // 테스트 CID
        params.put("partner_order_id", partnerOrderId);
        params.put("partner_user_id", String.valueOf(orderRequest.getMemberId()));
        params.put("item_name", orderRequest.getItemName());
        params.put("quantity", String.valueOf(orderRequest.getOrderItemRequestList().size()));
        params.put("total_amount", String.valueOf(orderRequest.getTotalPrice()));
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

        return kakaoPayResponseResponseEntity.getBody();

    }


    public KakaoPayApproveResponse approve(String pgToken, String tid, String partnerOrderId, String partnerUserId) throws IllegalAccessException {
        RestTemplate restTemplate = new RestTemplate();

        if (tid == null || partnerOrderId == null) {
            throw new IllegalAccessException("잘못된 접근입니다.");
        }


        Map<String, String> params = new HashMap<>();
        params.put("cid", "TC0ONETIME");
        params.put("tid", tid);
        params.put("partner_order_id", partnerOrderId);
        params.put("partner_user_id", partnerUserId);
        params.put("pg_token", pgToken);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(params, this.getHeaders());

        return restTemplate.postForObject(
                "https://open-api.kakaopay.com/online/v1/payment/approve",
                request,
                KakaoPayApproveResponse.class
        );
    }




}
