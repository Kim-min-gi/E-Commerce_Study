package com.study.ecommerce.request;

import com.study.ecommerce.domain.Address;
import com.study.ecommerce.domain.Member;
import com.study.ecommerce.domain.type.OrderStatus;
import com.study.ecommerce.domain.type.Payment;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderRequest {

    private Long orderId;

    private Payment payment;

    private Integer totalPrice;

    private Address address;

    private OrderStatus orderStatus;



}
