package com.study.ecommerce.request;

import com.study.ecommerce.domain.Address;
import com.study.ecommerce.domain.Order;
import com.study.ecommerce.domain.type.OrderStatus;
import com.study.ecommerce.domain.type.Payment;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class OrderRequest {

    private Long orderId;

    private Long memberId;

    private List<OrderItemRequest> orderItemRequestList;

    private Payment payment;

    private Long totalPrice;

    private Address address;

    private OrderStatus orderStatus;



}
