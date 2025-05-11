package com.study.ecommerce.response;

import com.study.ecommerce.domain.Order;
import com.study.ecommerce.domain.OrderProduct;
import com.study.ecommerce.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class OrderProductResponse {

    private String name;
    private int price;
    private int quantity;

    public static OrderProductResponse form(OrderProduct orderProduct){
        return OrderProductResponse.builder()
                .name(orderProduct.getProduct().getName())
                .price(orderProduct.getProduct().getPrice())
                .quantity(orderProduct.getQuantity())
                .build();
    }

}
