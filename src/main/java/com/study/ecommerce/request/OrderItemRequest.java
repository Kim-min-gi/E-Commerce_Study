package com.study.ecommerce.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderItemRequest {

    private Long productId;

    private Integer quantity;

    private Integer price;

}
