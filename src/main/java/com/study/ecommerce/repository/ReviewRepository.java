package com.study.ecommerce.repository;

import com.study.ecommerce.domain.OrderProduct;
import com.study.ecommerce.domain.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review,Long> {

    @EntityGraph(attributePaths = {"images"})
    Page<Review> findByProductId(Long productId, Pageable pageable);

    @EntityGraph(attributePaths = {"images"})
    Page<Review> findByMemberId(Long memberId, Pageable pageable);

    @Query("SELECT r FROM Review r LEFT JOIN FETCH r.images")
    List<Review> findAllWithImages();

}
