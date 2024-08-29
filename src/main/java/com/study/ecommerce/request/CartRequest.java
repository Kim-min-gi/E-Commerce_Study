package com.study.ecommerce.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CartRequest {

    @NotNull(message = "상품 아이디는 필수 입니다.")
    private Long productId;

    @NotNull(message = "상품 수량은 필수 입니다.")
    private Integer quantity;


}
