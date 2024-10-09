package com.study.ecommerce.response;

import com.study.ecommerce.domain.Order;
import com.study.ecommerce.domain.Product;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderProductResponse {

    private String name;
    private int price;
    private int quantity;

    public static OrderProductResponse form(Product product){
        return OrderProductResponse.builder()
                .name(product.getName())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .build();
    }

}
