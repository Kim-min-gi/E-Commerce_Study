package com.study.ecommerce.response;

import com.study.ecommerce.domain.Address;
import com.study.ecommerce.domain.type.OrderStatus;
import com.study.ecommerce.domain.type.Payment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class OrderResponse {

    private Long id;

    private int totalPrice;

    private OrderStatus orderStatus;


    //배송상세

    private Payment payment;

    private AddressResponse address;

    private LocalDateTime orderDate;



}
