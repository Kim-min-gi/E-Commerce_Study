package com.study.ecommerce.response;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductCategoryResponse {

    private Long id;

    private String name;

    private Long productCount;


}
