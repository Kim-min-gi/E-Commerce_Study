package com.study.ecommerce.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.study.ecommerce.domain.*;
import com.study.ecommerce.domain.Order;
import com.study.ecommerce.domain.type.OrderStatus;
import com.study.ecommerce.domain.type.Payment;
import com.study.ecommerce.repository.*;
import com.study.ecommerce.request.ProductRequest;
import com.study.ecommerce.request.ReviewRequest;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;

@ActiveProfiles("test")
@SpringBootTest
class ReviewServiceTest {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private OrderProductRepository orderProductRepository;

    @Autowired
    private OrderRepository orderRepository;

    @MockBean
    private AmazonS3 amazonS3;


    @BeforeEach
    void setUp() {
        cleanDatabase();
    }

    @AfterEach
    void tearDown() {
        cleanDatabase();
    }

    void cleanDatabase() {
        reviewRepository.deleteAll();
        orderProductRepository.deleteAll();
        orderRepository.deleteAll();
        productRepository.deleteAll();
        productCategoryRepository.deleteAll();
        memberRepository.deleteAll();
    }


    @Test
    @DisplayName("review save")
    public void saveReview() throws IOException {

        ProductCategory productCategory = ProductCategory.builder()
                .name("카테고리1")
                .build();

        productCategoryRepository.save(productCategory);


        Product product = Product.builder()
                .name("물품1")
                .price(3000)
                .quantity(3)
                .build();

        product.setCategory(productCategory);

        productRepository.save(product);

        Member member = Member.builder()
                .email("testing@gmail.com")
                .name("name")
                .password("!Aa123456")
                .role("ROLE_USER")
                .build();

        memberRepository.save(member);


        Address address = Address.builder()
                .city("서울")
                .street("강남")
                .zipcode("1111")
                .build();

        Order order =  Order.builder()
                .orderStatus(OrderStatus.ORDER_COMPLETE)
                .address(address)
                .payment(Payment.CARD)
                .member(member)
                .totalPrice(1000)
                .build();

        orderRepository.save(order);

        OrderProduct orderProduct = OrderProduct.builder()
                .order(order)
                .product(product)
                .quantity(1)
                .build();

        order.addOrderProduct(orderProduct);

        orderProductRepository.save(orderProduct);


        MockMultipartFile file = new MockMultipartFile(
                "images",
                "test-image.jpg",
                "image/jpeg",
                "dummy content".getBytes()
        );

        ReviewRequest reviewRequest = ReviewRequest.builder()
                .memberId(member.getId())
                .memberName("홍길동")
                .orderId(order.getId())
                .productId(product.getId())
                .rating(3)
                .content("그럭저럭")
                .build();

        // when: amazonS3.getUrl(), putObject, listObjectsV2 mocking
        Mockito.when(amazonS3.putObject(ArgumentMatchers.any(PutObjectRequest.class))).thenReturn(new PutObjectResult());

        URL fakeUrl = new URL("https://fake-s3-url.com/test-image.jpg");
        Mockito.when(amazonS3.getUrl(anyString(), anyString())).thenReturn(fakeUrl);

        // then
        reviewService.writeReview(reviewRequest, List.of(file));


        List<Review> all = reviewRepository.findAllWithImages();
        Review review = all.get(0);

        Assertions.assertEquals("홍길동", review.getMemberName());
        Assertions.assertEquals("https://fake-s3-url.com/test-image.jpg",review.getImages().get(0).getImageUrl());
        Assertions.assertEquals(3,review.getRating());
        Assertions.assertEquals("그럭저럭",review.getContent());

    }




    @Test
    @DisplayName("productId review 조회")
    public void getReviewsByProductId() throws IOException {

        ProductCategory productCategory = ProductCategory.builder()
                .name("카테고리1")
                .build();

        productCategoryRepository.save(productCategory);


        Product product = Product.builder()
                .name("물품1")
                .price(3000)
                .quantity(3)
                .build();

        product.setCategory(productCategory);

        productRepository.save(product);

        Member member = Member.builder()
                .email("testing@gmail.com")
                .name("name")
                .password("!Aa123456")
                .role("ROLE_USER")
                .build();

        memberRepository.save(member);


        Address address = Address.builder()
                .city("서울")
                .street("강남")
                .zipcode("1111")
                .build();

        Order order =  Order.builder()
                .orderStatus(OrderStatus.ORDER_COMPLETE)
                .address(address)
                .payment(Payment.CARD)
                .member(member)
                .totalPrice(1000)
                .build();

        orderRepository.save(order);

        OrderProduct orderProduct = OrderProduct.builder()
                .order(order)
                .product(product)
                .quantity(1)
                .build();

        order.addOrderProduct(orderProduct);

        orderProductRepository.save(orderProduct);


        MockMultipartFile file = new MockMultipartFile(
                "images",
                "test-image.jpg",
                "image/jpeg",
                "dummy content".getBytes()
        );

//        ReviewRequest reviewRequest = ReviewRequest.builder()
//                .memberId(member.getId())
//                .memberName("홍길동")
//                .orderId(order.getId())
//                .productId(product.getId())
//                .rating(3)
//                .content("그럭저럭")
//                .build();

        List<ReviewRequest> reviewRequest = IntStream.range(1,21).mapToObj(i ->
                ReviewRequest.builder()
                        .memberId(member.getId())
                        .memberName("홍길동" + i)
                        .orderId(order.getId())
                        .productId(product.getId())
                        .rating(3)
                        .content("그럭저럭" + i)
                        .build()
        ).toList();


        // when: amazonS3.getUrl(), putObject, listObjectsV2 mocking
        Mockito.when(amazonS3.putObject(ArgumentMatchers.any(PutObjectRequest.class))).thenReturn(new PutObjectResult());

        URL fakeUrl = new URL("https://fake-s3-url.com/test-image.jpg");
        Mockito.when(amazonS3.getUrl(anyString(), anyString())).thenReturn(fakeUrl);

        Pageable pageable = PageRequest.of(0,10);

        // then
        for (ReviewRequest review : reviewRequest) {
            reviewService.writeReview(review, List.of(file));
        }

        Page<Review> reviews = reviewRepository.findByProductId(product.getId(), pageable);

        List<Review> content = reviews.getContent();
        Review review = content.get(0);

        Assertions.assertEquals(10,reviews.get().count());
        Assertions.assertEquals(2,reviews.getTotalPages());
        Assertions.assertEquals(20,reviews.getTotalElements());
        Assertions.assertEquals("홍길동1",review.getMemberName());
        Assertions.assertEquals("그럭저럭1",review.getContent());

    }


    @Test
    @DisplayName("memberId review 조회")
    public void getReviewsByMemberId() throws IOException {

        ProductCategory productCategory = ProductCategory.builder()
                .name("카테고리1")
                .build();

        productCategoryRepository.save(productCategory);


        Product product = Product.builder()
                .name("물품1")
                .price(3000)
                .quantity(3)
                .build();

        product.setCategory(productCategory);

        productRepository.save(product);

        Member member = Member.builder()
                .email("testing@gmail.com")
                .name("name")
                .password("!Aa123456")
                .role("ROLE_USER")
                .build();

        memberRepository.save(member);


        Address address = Address.builder()
                .city("서울")
                .street("강남")
                .zipcode("1111")
                .build();

        Order order =  Order.builder()
                .orderStatus(OrderStatus.ORDER_COMPLETE)
                .address(address)
                .payment(Payment.CARD)
                .member(member)
                .totalPrice(1000)
                .build();

        orderRepository.save(order);

        OrderProduct orderProduct = OrderProduct.builder()
                .order(order)
                .product(product)
                .quantity(1)
                .build();

        order.addOrderProduct(orderProduct);

        orderProductRepository.save(orderProduct);


        MockMultipartFile file = new MockMultipartFile(
                "images",
                "test-image.jpg",
                "image/jpeg",
                "dummy content".getBytes()
        );

//        ReviewRequest reviewRequest = ReviewRequest.builder()
//                .memberId(member.getId())
//                .memberName("홍길동")
//                .orderId(order.getId())
//                .productId(product.getId())
//                .rating(3)
//                .content("그럭저럭")
//                .build();

        List<ReviewRequest> reviewRequest = IntStream.range(1,31).mapToObj(i ->
                ReviewRequest.builder()
                        .memberId(member.getId())
                        .memberName("홍길동" + i)
                        .orderId(order.getId())
                        .productId(product.getId())
                        .rating(3)
                        .content("그럭저럭" + i)
                        .build()
        ).toList();


        // when: amazonS3.getUrl(), putObject, listObjectsV2 mocking
        Mockito.when(amazonS3.putObject(ArgumentMatchers.any(PutObjectRequest.class))).thenReturn(new PutObjectResult());

        URL fakeUrl = new URL("https://fake-s3-url.com/test-image.jpg");
        Mockito.when(amazonS3.getUrl(anyString(), anyString())).thenReturn(fakeUrl);

        Pageable pageable = PageRequest.of(1,10);

        // then
        for (ReviewRequest review : reviewRequest) {
            reviewService.writeReview(review, List.of(file));
        }

        Page<Review> reviews = reviewRepository.findByMemberId(member.getId(), pageable);

        List<Review> content = reviews.getContent();
        Review review = content.get(0);

        Assertions.assertEquals(10,reviews.get().count());
        Assertions.assertEquals(3,reviews.getTotalPages());
        Assertions.assertEquals(30,reviews.getTotalElements());
        Assertions.assertEquals("홍길동11",review.getMemberName());
        Assertions.assertEquals("그럭저럭11",review.getContent());

    }

    @Test
    @DisplayName("리뷰 수정")
    @Transactional
    public void modifyReview() throws IOException, IllegalAccessException {

        ProductCategory productCategory = ProductCategory.builder()
                .name("카테고리1")
                .build();

        productCategoryRepository.save(productCategory);


        Product product = Product.builder()
                .name("물품1")
                .price(3000)
                .quantity(3)
                .build();

        product.setCategory(productCategory);

        productRepository.save(product);

        Member member = Member.builder()
                .email("Testing@naver.com")
                .name("name")
                .password("!Aa123456")
                .role("ROLE_USER")
                .build();

        memberRepository.save(member);


        Address address = Address.builder()
                .city("서울")
                .street("강남")
                .zipcode("1111")
                .build();

        Order order =  Order.builder()
                .orderStatus(OrderStatus.ORDER_COMPLETE)
                .address(address)
                .payment(Payment.CARD)
                .member(member)
                .totalPrice(1000)
                .build();

        orderRepository.save(order);

        OrderProduct orderProduct = OrderProduct.builder()
                .order(order)
                .product(product)
                .quantity(1)
                .build();

        order.addOrderProduct(orderProduct);

        orderProductRepository.save(orderProduct);


        MockMultipartFile file = new MockMultipartFile(
                "images",
                "test-image.jpg",
                "image/jpeg",
                "dummy content".getBytes()
        );

        ReviewRequest reviewRequest = ReviewRequest.builder()
                .memberId(member.getId())
                .memberName("홍길동")
                .orderId(order.getId())
                .productId(product.getId())
                .rating(3)
                .content("그럭저럭")
                .build();



        // when: amazonS3.getUrl(), putObject, listObjectsV2 mocking
        Mockito.when(amazonS3.putObject(ArgumentMatchers.any(PutObjectRequest.class))).thenReturn(new PutObjectResult());

        URL fakeUrl = new URL("https://fake-s3-url.com/test-image.jpg");
        Mockito.when(amazonS3.getUrl(anyString(), anyString())).thenReturn(fakeUrl);

        Pageable pageable = PageRequest.of(0,10);

        List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("Testing@naver.com", null, authorities)
        );


        // then

        reviewService.writeReview(reviewRequest, List.of(file));


        Page<Review> reviews = reviewRepository.findByMemberId(member.getId(), pageable);

        List<Review> content = reviews.getContent();
        Review review = content.get(0);


        ReviewRequest modifyRequest = ReviewRequest.builder()
                .memberId(member.getId())
                .memberName("홍길동11")
                .orderId(order.getId())
                .productId(product.getId())
                .rating(1)
                .content("그럭저럭11")
                .build();


        MockMultipartFile file2 = new MockMultipartFile(
                "images",
                "test-image2.jpg",
                "image/jpeg",
                "dummy content".getBytes()
        );


        reviewService.modifyReview(review.getId(),modifyRequest,List.of(file2));

        Review review1 = reviewRepository.findById(review.getId()).get();


        Assertions.assertEquals("그럭저럭11",review1.getContent());


    }

    @Test
    @DisplayName("리뷰 삭제")
    @Transactional
    public void deleteReview() throws IOException, IllegalAccessException {

        ProductCategory productCategory = ProductCategory.builder()
                .name("카테고리1")
                .build();

        productCategoryRepository.save(productCategory);


        Product product = Product.builder()
                .name("물품1")
                .price(3000)
                .quantity(3)
                .build();

        product.setCategory(productCategory);

        productRepository.save(product);

        Member member = Member.builder()
                .email("Testing@naver.com")
                .name("name")
                .password("!Aa123456")
                .role("ROLE_USER")
                .build();

        memberRepository.save(member);


        Address address = Address.builder()
                .city("서울")
                .street("강남")
                .zipcode("1111")
                .build();

        Order order =  Order.builder()
                .orderStatus(OrderStatus.ORDER_COMPLETE)
                .address(address)
                .payment(Payment.CARD)
                .member(member)
                .totalPrice(1000)
                .build();

        orderRepository.save(order);

        OrderProduct orderProduct = OrderProduct.builder()
                .order(order)
                .product(product)
                .quantity(1)
                .build();

        order.addOrderProduct(orderProduct);

        orderProductRepository.save(orderProduct);


        MockMultipartFile file = new MockMultipartFile(
                "images",
                "test-image.jpg",
                "image/jpeg",
                "dummy content".getBytes()
        );

        ReviewRequest reviewRequest = ReviewRequest.builder()
                .memberId(member.getId())
                .memberName("홍길동")
                .orderId(order.getId())
                .productId(product.getId())
                .rating(3)
                .content("그럭저럭")
                .build();



        // when: amazonS3.getUrl(), putObject, listObjectsV2 mocking
        Mockito.when(amazonS3.putObject(ArgumentMatchers.any(PutObjectRequest.class))).thenReturn(new PutObjectResult());

        URL fakeUrl = new URL("https://fake-s3-url.com/test-image.jpg");
        Mockito.when(amazonS3.getUrl(anyString(), anyString())).thenReturn(fakeUrl);

        Pageable pageable = PageRequest.of(0,10);

        List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("Testing@naver.com", null, authorities)
        );


        // then
        reviewService.writeReview(reviewRequest, List.of(file));


        Review savedReview = reviewRepository.findAll().get(0);
        Long reviewId = savedReview.getId();


        reviewService.deleteReview(reviewId);

        Optional<Review> deletedReview = reviewRepository.findById(reviewId);
        Assertions.assertTrue(deletedReview.isEmpty(), "리뷰가 삭제되지 않았습니다.");

    }



}