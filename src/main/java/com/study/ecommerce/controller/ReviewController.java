package com.study.ecommerce.controller;

import com.study.ecommerce.request.ReviewRequest;
import com.study.ecommerce.response.ReviewResponse;
import com.study.ecommerce.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReviewController {


    private final ReviewService reviewService;


    @GetMapping("/product/reviews")
    public ResponseEntity<Page<ReviewResponse>> getProductReviews(@PageableDefault Pageable pageable, @Valid @RequestParam Long productId){

        Page<ReviewResponse> reviews = reviewService.getProductReviews(pageable, productId);

        return ResponseEntity.ok(reviews);
    }


    @GetMapping("/member/reviews")
    public ResponseEntity<Page<ReviewResponse>> getMemberReviews(@PageableDefault Pageable pageable,@Valid @RequestParam Long productId){

        Page<ReviewResponse> reviews = reviewService.getMemberReviews(pageable, productId);

        return ResponseEntity.ok(reviews);
    }


    @PostMapping(value = "/writeReview",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> writeReviews(@RequestPart("request") @Valid ReviewRequest reviewRequest, @RequestPart("images") List<MultipartFile> images) throws IOException {

        reviewService.writeReview(reviewRequest, images);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/deleteReview")
    public ResponseEntity<?> deleteReview(@RequestBody Long reviewId) throws IllegalAccessException {

        reviewService.deleteReview(reviewId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @PatchMapping("/modifyReview")
    public ResponseEntity<?> modifyReview(@RequestBody ReviewRequest reviewRequest, @RequestPart("images") List<MultipartFile> images) throws IllegalAccessException, IOException {

        reviewService.modifyReview(reviewRequest,images);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }





}
