package com.study.ecommerce.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Builder
@Getter
public class ReviewRequest {

    private Long id;

    @NotNull
    private Long memberId;

    @NotNull
    private String memberName;

    @NotNull
    private Long orderId;

    @NotNull
    private Long productId;

    @NotNull
    private int rating;

    @NotBlank
    private String content;

}
