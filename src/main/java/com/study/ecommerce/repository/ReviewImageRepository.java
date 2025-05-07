package com.study.ecommerce.repository;

import com.study.ecommerce.domain.Review;
import com.study.ecommerce.domain.ReviewImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewImageRepository extends JpaRepository<ReviewImage,Long> {

    List<ReviewImage> findByReviewId(Long reviewId);

}
