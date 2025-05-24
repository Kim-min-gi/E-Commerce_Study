package com.study.ecommerce.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {
    private OrderRequest orderRequest;
    private String partnerOrderId;
    private String pgToken;
    private String tid;
    private String partnerUserId;
}
