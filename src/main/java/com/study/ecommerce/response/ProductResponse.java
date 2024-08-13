package com.study.ecommerce.response;

import com.study.ecommerce.domain.Product;
import lombok.*;

@AllArgsConstructor
@Getter
@Builder
public class ProductResponse {

    private Long id;
    private String name;
    private int price;
    private int quantity;
    private String categoryName;


    public static ProductResponse form(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .categoryName(product.getProductCategory().getName())
                .build();
    }




}
