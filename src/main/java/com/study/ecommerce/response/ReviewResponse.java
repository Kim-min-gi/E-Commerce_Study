package com.study.ecommerce.response;

import com.study.ecommerce.domain.Review;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

@Builder
public class ReviewResponse {

    private Long id;

    private Long memberId;

    private Long productId;

    private int rating;

    private String content;

    private final List<ReviewImageResponse> images;

    public static ReviewResponse from(Review review){

        return ReviewResponse.builder()
                .id(review.getId())
                .memberId(review.getMemberId())
                .productId(review.getProductId())
                .rating(review.getRating())
                .content(review.getContent())
                .images(
                        review.getImages().stream().map(image -> ReviewImageResponse.builder()
                                .id(image.getId())
                                .imageUrl(image.getImageUrl())
                                .build()).toList()
                )
                .build();
    }


}
