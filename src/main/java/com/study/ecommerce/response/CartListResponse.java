package com.study.ecommerce.response;


import com.study.ecommerce.domain.Cart;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CartListResponse {


    private Long id;

    private Long productId;

    private String productName;

    private Integer quantity;

    public static CartListResponse from(Cart cart){
        return CartListResponse.builder()
                .id(cart.getId())
                .productId(cart.getCartProduct().getId())
                .productName(cart.getCartProduct().getName())
                .quantity(cart.getQuantity())
                .build();
    }



}
