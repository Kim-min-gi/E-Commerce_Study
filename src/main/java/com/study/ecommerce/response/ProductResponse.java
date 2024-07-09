package com.study.ecommerce.response;

import com.study.ecommerce.domain.Product;
import com.study.ecommerce.domain.ProductCategory;
import lombok.*;

import java.util.Optional;

@AllArgsConstructor
@Getter
@Builder
public class ProductResponse {

    private Long id;
    private String name;
    private int price;
    private int amount;


    public static ProductResponse from(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .amount(product.getAmount())
                .build();
    }




}
