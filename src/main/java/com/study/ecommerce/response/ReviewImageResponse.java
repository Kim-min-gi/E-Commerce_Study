package com.study.ecommerce.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReviewImageResponse {

    private Long id;

    private String imageUrl;

}
