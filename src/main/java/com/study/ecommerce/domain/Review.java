package com.study.ecommerce.domain;

import com.study.ecommerce.common.BaseTimeEntity;
import com.study.ecommerce.request.ReviewRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseTimeEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn
//    private Member member;

    @NotNull
    private Long memberId;

    @NotBlank
    private String memberName;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn
//    private OrderProduct orderProduct;

    @NotNull
    private Long orderId;

    @NotNull
    private Long productId;

    @Column(nullable = false)
    private int rating;

    private String content;

    @OneToMany(mappedBy = "review" , cascade = CascadeType.ALL)
    private List<ReviewImage> images = new ArrayList<>();


    @Builder
    public Review(Long memberId, String memberName, Long orderId, Long productId, int rating, String content) {
        this.memberId = memberId;
        this.memberName = memberName;
        this.orderId = orderId;
        this.productId = productId;
        this.rating = rating;
        this.content = content;
    }


    public static Review from(ReviewRequest reviewRequest){
        return Review.builder()
                .memberId(reviewRequest.getMemberId())
                .memberName(reviewRequest.getMemberName())
                .orderId(reviewRequest.getOrderId())
                .productId(reviewRequest.getProductId())
                .rating(reviewRequest.getRating())
                .content(reviewRequest.getContent())
                .build();
    }

    public void addImage(ReviewImage image) {
        this.images.add(image);
        image.setReview(this);
    }

    public void modifyContent(String content){
        this.content = content;
    }


}
