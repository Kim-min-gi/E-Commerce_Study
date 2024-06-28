package com.study.ecommerce.request;

import com.study.ecommerce.domain.ProductCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductRequest {

    @NotBlank(message = "상품명은 필수 입니다.")
    private String name;

    @NotBlank(message = "상품가격은 필수 입니다.")
    private int price;

    @NotBlank(message = "수량은 필수 입니다.")
    private int amount;

    @NotBlank(message = "카테고리는 필수 입니다.")
    private ProductCategory productCategory;


}
