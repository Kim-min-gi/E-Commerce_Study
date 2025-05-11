package com.study.ecommerce.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemRequest implements Serializable {

    private Long productId;

    private Integer quantity;

    private Integer price;

}
