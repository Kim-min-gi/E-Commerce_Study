package com.study.ecommerce.request;

import com.study.ecommerce.domain.ProductCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductRequest {

    private String name;

    private Integer price;

    private Integer amount;

    private String categoryName;


}
