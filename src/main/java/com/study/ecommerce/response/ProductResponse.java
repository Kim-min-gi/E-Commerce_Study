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
    private String categoryName;


    public static ProductResponse form(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .amount(product.getAmount())
                .categoryName(product.getProductCategory().getName())
                .build();
    }




}
