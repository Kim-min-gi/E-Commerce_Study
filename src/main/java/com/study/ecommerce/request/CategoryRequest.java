package com.study.ecommerce.request;


import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CategoryRequest {

    @NotNull(message = "카테고리명은 필수 입니다.")
    private String name;


}
