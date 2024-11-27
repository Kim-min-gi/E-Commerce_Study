package com.study.ecommerce.response;

import com.study.ecommerce.domain.Address;
import com.study.ecommerce.domain.Order;
import com.study.ecommerce.domain.type.OrderStatus;
import com.study.ecommerce.domain.type.Payment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class OrderResponse {

    private Long id;

    private int totalPrice;

    private OrderStatus orderStatus;

    private List<OrderProductResponse> orderProductResponse;


    //배송상세

    private Payment payment;

    private AddressResponse address;

    private LocalDateTime orderDate;


    public void addOrderProductResponse(OrderProductResponse orderProductResponse){
        this.orderProductResponse.add(orderProductResponse);
    }


    public static OrderResponse form(Order order){
        return OrderResponse.builder()
                .id(order.getId())
                .address(Address.form(order.getAddress()))
                .payment(order.getPayment())
                .orderStatus(order.getOrderStatus())
                .totalPrice(order.getTotalPrice())
                .build();
    }

}
