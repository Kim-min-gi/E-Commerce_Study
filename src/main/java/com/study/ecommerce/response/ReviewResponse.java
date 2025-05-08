package com.study.ecommerce.response;

import com.study.ecommerce.domain.Review;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class ReviewResponse {

    private Long id;

    private Long memberId;

    private String memberName;

    private Long productId;

    private int rating;

    private String content;

    private final List<ReviewImageResponse> images;

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;

    public static ReviewResponse from(Review review){

        return ReviewResponse.builder()
                .id(review.getId())
                .memberId(review.getMemberId())
                .memberName(review.getMemberName())
                .productId(review.getProductId())
                .rating(review.getRating())
                .content(review.getContent())
                .images(
                        review.getImages().stream().map(image -> ReviewImageResponse.builder()
                                .id(image.getId())
                                .imageUrl(image.getImageUrl())
                                .build()).toList()
                )
                .createdDate(review.getCreatedDate())
                .modifiedDate(review.getModifiedDate())
                .build();
    }


}
