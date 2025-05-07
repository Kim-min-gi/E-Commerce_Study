package com.study.ecommerce.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.study.ecommerce.domain.Member;
import com.study.ecommerce.domain.Review;
import com.study.ecommerce.domain.ReviewImage;
import com.study.ecommerce.exception.NotFoundReviewException;
import com.study.ecommerce.repository.MemberRepository;
import com.study.ecommerce.repository.ReviewImageRepository;
import com.study.ecommerce.repository.ReviewRepository;
import com.study.ecommerce.request.ReviewRequest;
import com.study.ecommerce.response.ReviewResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final AmazonS3 amazonS3;
    private final ReviewRepository reviewRepository;
    private final ReviewImageRepository reviewImageRepository;
    private final MemberRepository memberRepository;



    @Transactional(readOnly = true)
    public Page<ReviewResponse> getProductReviews(Pageable pageable, Long productId){

        Page<Review> productIdReviews = reviewRepository.findByProductId(productId, pageable);

        return  productIdReviews.map(ReviewResponse::from);
    }


    @Transactional(readOnly = true)
    public Page<ReviewResponse> getMemberReviews(Pageable pageable, Long memberId){

        Page<Review> productIdReviews = reviewRepository.findByMemberId(memberId, pageable);

        return  productIdReviews.map(ReviewResponse::from);
    }


    @Transactional
    public void writeReview(ReviewRequest reviewRequest, List<MultipartFile> images) throws IOException {
        Review review = Review.from(reviewRequest);

        for (MultipartFile file : images){
            String url = uploadFile(file);
            ReviewImage reviewImage = ReviewImage.builder()
                    .imageUrl(url)
                    .review(review)
                    .build();

            review.addImage(reviewImage);
        }

        reviewRepository.save(review);

    }

    @Transactional
    public void modifyReview(ReviewRequest reviewRequest, List<MultipartFile> images) throws IllegalAccessException, IOException {
        Review review = reviewRepository.findById(reviewRequest.getId()).orElseThrow(NotFoundReviewException::new);

        Optional<Member> member = memberRepository.findByEmail(getMemberEmail());

        if (member.isEmpty() || !Objects.equals(member.get().getId(), review.getMemberId())){
            throw new IllegalAccessException("글 작성자만 수정 가능합니다.");
        }

        if (!images.isEmpty()){

            for (ReviewImage image : review.getImages()){
                deleteFile(image.getImageUrl());
            }

            review.getImages().clear();

            for (MultipartFile file : images){
                String url = uploadFile(file);
                ReviewImage reviewImage = ReviewImage.builder()
                        .imageUrl(url)
                        .review(review)
                        .build();

                review.addImage(reviewImage);
            }
        }

        review.modifyContent(reviewRequest.getContent());

//        reviewRepository.save(review);

    }


    public void deleteReview(Long reviewId) throws IllegalAccessException {
        Review review = reviewRepository.findById(reviewId).orElseThrow(NotFoundReviewException::new);

        Optional<Member> member = memberRepository.findByEmail(getMemberEmail());

        if (member.isEmpty() || !Objects.equals(member.get().getId(), review.getMemberId())){
            throw new IllegalAccessException("관리자 및 글 작성자만 삭제 가능합니다.");
        }

        //s3 delete images
        for (ReviewImage image : review.getImages()){
            deleteFile(image.getImageUrl());
        }

        reviewRepository.delete(review);
    }




    public String getMemberEmail(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }


    //file upload s3
    public String uploadFile(MultipartFile multipartFile) throws IOException {
        String fileName = multipartFile.getOriginalFilename();

        //파일 형식 구하기
//        String ext = fileName.split("\\.")[1];
        String ext =  StringUtils.getFilenameExtension(fileName);
        String contentType = "";

        //content type을 지정해서 올려주지 않으면 자동으로 "application/octet-stream"으로 고정이 되서 링크 클릭시 웹에서 열리는게 아니라 자동 다운이 시작됨.
        if (ext != null) {
            switch (ext.toLowerCase()) {
                case "jpg":
                case "jpeg":
                    contentType = "image/jpeg";
                    break;
                case "png":
                    contentType = "image/png";
                    break;
                case "txt":
                    contentType = "text/plain";
                    break;
                case "csv":
                    contentType = "text/csv";
                    break;
            }
        }

        // contentType 누락 시 기본값 설정
        if (contentType.isEmpty()) {
            contentType = "application/octet-stream";
        }

        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(contentType);

            amazonS3.putObject(new PutObjectRequest(bucket, fileName, multipartFile.getInputStream(), metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (SdkClientException e) {
            log.error("S3 파일 업로드 실패 - 파일명: {}, 에러: {}", fileName, e.getMessage(), e);
            throw new RuntimeException("이미지 업로드 중 오류 발생", e);
        }

//        //object 정보 가져오기 log용 삭제 요망
//        ListObjectsV2Result listObjectsV2Result = amazonS3.listObjectsV2(bucket);
//        List<S3ObjectSummary> objectSummaries = listObjectsV2Result.getObjectSummaries();
//
//        for (S3ObjectSummary object: objectSummaries) {
//            log.info("object = {}",object.toString());
//        }

        log.info("파일 {} 업로드 완료", fileName);

        return amazonS3.getUrl(bucket, fileName).toString();
    }


    //파일 삭제
    public void deleteFile(String fileUrl){
        String key = fileUrl.substring(fileUrl.indexOf(bucket) + bucket.length() + 1);

        amazonS3.deleteObject(new DeleteObjectRequest(bucket,key));
    }



}
