package com.study.ecommerce.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.ecommerce.PageResponseFieldsSnippet;
import com.study.ecommerce.config.CustomMockMember;
import com.study.ecommerce.domain.*;
import com.study.ecommerce.domain.Order;
import com.study.ecommerce.domain.type.OrderStatus;
import com.study.ecommerce.domain.type.Payment;
import com.study.ecommerce.repository.*;
import com.study.ecommerce.request.ReviewRequest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme = "https",uriHost = "api.ecommerce.com", uriPort = 433)
@ExtendWith(RestDocumentationExtension.class)
class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

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
        orderRepository.deleteAll();
        productRepository.deleteAll();
        orderProductRepository.deleteAll();
        productCategoryRepository.deleteAll();
        memberRepository.deleteAll();
        reviewRepository.deleteAll();
    }



    @Test
    @DisplayName("상품 리뷰 조회")
    @CustomMockMember
    void getProductReviews() throws Exception {
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

        ReviewRequest reviewRequest = ReviewRequest.builder()
                .memberId(member.getId())
                .memberName("홍길동")
                .orderId(order.getId())
                .productId(product.getId())
                .rating(3)
                .content("그럭저럭")
                .build();


        Review review = Review.from(reviewRequest);

        reviewRepository.save(review);



        mockMvc.perform(RestDocumentationRequestBuilders.get("/reviews/product/{productId}?page=1",product.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath()
                .andDo(MockMvcRestDocumentation.document("review/product-reviews",
                        RequestDocumentation.pathParameters(
                                RequestDocumentation.parameterWithName("productId").description("상품 아이디")
                        ),
                        PayloadDocumentation.responseFields(
                                PageResponseFieldsSnippet.getPageResponseFields(
                                subsectionWithPath("content").description("리뷰 목록"),
                                PayloadDocumentation.fieldWithPath("content[].id").description("리뷰 아이디 (처음 작성땐 NULL)"),
                                PayloadDocumentation.fieldWithPath("content[].memberId").description("리뷰 작성자 아이디"),
                                PayloadDocumentation.fieldWithPath("content[].memberName").description("리뷰 작성자 닉네임"),
                                PayloadDocumentation.fieldWithPath("content[].productId").description("상품 아이디"),
                                PayloadDocumentation.fieldWithPath("content[].rating").description("평점"),
                                PayloadDocumentation.fieldWithPath("content[].content").description("리뷰 내용"),
                                PayloadDocumentation.fieldWithPath("content[].createdDate").description("리뷰 작성 시간"),
                                PayloadDocumentation.fieldWithPath("content[].modifiedDate").description("리뷰 수정 시간"),
                                subsectionWithPath("content[].images").description("리뷰 이미지 리스트"),
                                fieldWithPath("content[].images[].id").type(JsonFieldType.NUMBER).description("이미지 아이디").optional(),
                                fieldWithPath("content[].images[].imageUrl").type(JsonFieldType.STRING).description("이미지 URL").optional()
                                ).toArray(new FieldDescriptor[0])

                        )
                ))
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    @DisplayName("사용자 리뷰 조회")
    @CustomMockMember
    void getMemberReviews() throws Exception {
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

        ReviewRequest reviewRequest = ReviewRequest.builder()
                .memberId(member.getId())
                .memberName("홍길동")
                .orderId(order.getId())
                .productId(product.getId())
                .rating(3)
                .content("그럭저럭")
                .build();


        Review review = Review.from(reviewRequest);

        reviewRepository.save(review);



        mockMvc.perform(RestDocumentationRequestBuilders.get("/reviews/member/{memberId}?page=1",member.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath()
                .andDo(MockMvcRestDocumentation.document("review/member-reviews",
                        RequestDocumentation.pathParameters(
                                RequestDocumentation.parameterWithName("memberId").description("회원 아이디")
                        ),
                        PayloadDocumentation.responseFields(
                                PageResponseFieldsSnippet.getPageResponseFields(
                                        subsectionWithPath("content").description("리뷰 목록"),
                                        PayloadDocumentation.fieldWithPath("content[].id").description("리뷰 아이디 (처음 작성땐 NULL)"),
                                        PayloadDocumentation.fieldWithPath("content[].memberId").description("리뷰 작성자 아이디"),
                                        PayloadDocumentation.fieldWithPath("content[].memberName").description("리뷰 작성자 닉네임"),
                                        PayloadDocumentation.fieldWithPath("content[].productId").description("상품 아이디"),
                                        PayloadDocumentation.fieldWithPath("content[].rating").description("평점"),
                                        PayloadDocumentation.fieldWithPath("content[].content").description("리뷰 내용"),
                                        PayloadDocumentation.fieldWithPath("content[].createdDate").description("리뷰 작성 시간"),
                                        PayloadDocumentation.fieldWithPath("content[].modifiedDate").description("리뷰 수정 시간"),
                                        subsectionWithPath("content[].images").description("리뷰 이미지 리스트"),
                                        fieldWithPath("content[].images[].id").type(JsonFieldType.NUMBER).description("이미지 아이디").optional(),
                                        fieldWithPath("content[].images[].imageUrl").type(JsonFieldType.STRING).description("이미지 URL").optional()
                                ).toArray(new FieldDescriptor[0])

                        )
                ))
                .andDo(MockMvcResultHandlers.print());


    }

    @Test
    @DisplayName("리뷰 작성")
    @CustomMockMember
    void writeReviews() throws Exception {
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


        MockMultipartFile jsonPart = new MockMultipartFile(
                "request",                 // @RequestPart 이름
                "",                        // 파일 이름 (빈 문자열 가능)
                "application/json",        // MIME 타입
                objectMapper.writeValueAsBytes(reviewRequest) // JSON 직렬화
        );


        mockMvc.perform(RestDocumentationRequestBuilders.multipart("/review")
                        .file(jsonPart)
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(MockMvcResultMatchers.status().isCreated())
//                .andExpect(MockMvcResultMatchers.jsonPath()
                .andDo(MockMvcRestDocumentation.document("review/write-review",
                        RequestDocumentation.requestParts(
                                RequestDocumentation.partWithName("request").description("리뷰 데이터 (JSON)"),
                                RequestDocumentation.partWithName("images").description("첨부 이미지 파일들")

                        ),
                        PayloadDocumentation.requestPartFields("request",
                                PayloadDocumentation.fieldWithPath("memberId").description("리뷰 작성자 아이디"),
                                PayloadDocumentation.fieldWithPath("memberName").description("리뷰 작성자"),
                                PayloadDocumentation.fieldWithPath("orderId").description("주문 번호"),
                                PayloadDocumentation.fieldWithPath("productId").description("상품 번호"),
                                PayloadDocumentation.fieldWithPath("rating").description("평점"),
                                PayloadDocumentation.fieldWithPath("content").description("리뷰 내용")
                        )
                ))
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    @DisplayName("리뷰 삭제")
    @CustomMockMember
    void deleteReview() throws Exception {
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

        ReviewRequest reviewRequest = ReviewRequest.builder()
                .memberId(member.getId())
                .memberName("홍길동")
                .orderId(order.getId())
                .productId(product.getId())
                .rating(3)
                .content("그럭저럭")
                .build();


        Review review = Review.from(reviewRequest);

        reviewRepository.save(review);



        mockMvc.perform(RestDocumentationRequestBuilders.delete("/reviews/{reviewId}",review.getId()))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
//                .andExpect(MockMvcResultMatchers.jsonPath()
                .andDo(MockMvcRestDocumentation.document("review/deleteReview",
                        RequestDocumentation.pathParameters(
                                RequestDocumentation.parameterWithName("reviewId").description("리뷰 아이디")
                        )
                ))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("리뷰 수정")
    @CustomMockMember
    void modifyReview() throws Exception {
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

        ReviewRequest reviewRequest = ReviewRequest.builder()
                .memberId(member.getId())
                .memberName("홍길동")
                .orderId(order.getId())
                .productId(product.getId())
                .rating(3)
                .content("그럭저럭")
                .build();


        Review review = Review.from(reviewRequest);

        reviewRepository.save(review);

        ReviewRequest reviewRequest2 = ReviewRequest.builder()
                .memberId(member.getId())
                .memberName("홍길동11")
                .orderId(order.getId())
                .productId(product.getId())
                .rating(3)
                .content("그럭저럭11")
                .build();

        // multipart: JSON 데이터
        MockMultipartFile jsonPart = new MockMultipartFile(
                "request",
                "",
                "application/json",
                objectMapper.writeValueAsBytes(reviewRequest2)
        );

        // multipart: 이미지 데이터
        MockMultipartFile image = new MockMultipartFile(
                "images",
                "new-image.jpg",
                "image/jpeg",
                "new dummy image content".getBytes()
        );

        URL fakeUrl = new URL("https://fake-s3-url.com/test-image.jpg");
        Mockito.when(amazonS3.getUrl(anyString(), anyString())).thenReturn(fakeUrl);



        mockMvc.perform( RestDocumentationRequestBuilders.multipart("/reviews/{reviewId}", review.getId())
                        .file(jsonPart)
                        .file(image)
                        .with(request -> {
                            request.setMethod("PATCH"); // Multipart는 기본 POST라서 PATCH로 바꿔줘야 함
                            return request;
                        })
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                )
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(MockMvcRestDocumentation.document("review/modify-review",
                        RequestDocumentation.pathParameters(
                                RequestDocumentation.parameterWithName("reviewId").description("리뷰 아이디")
                        ),
                        RequestDocumentation.requestParts(
                                RequestDocumentation.partWithName("request").description("수정할 리뷰 데이터 (JSON)"),
                                RequestDocumentation.partWithName("images").description("리뷰 이미지 파일 (선택)").optional()
                        ),
                        PayloadDocumentation.requestPartFields("request",
                                PayloadDocumentation.fieldWithPath("memberId").description("리뷰 작성자 ID"),
                                PayloadDocumentation.fieldWithPath("memberName").description("작성자 이름"),
                                PayloadDocumentation.fieldWithPath("orderId").description("주문 ID"),
                                PayloadDocumentation.fieldWithPath("productId").description("상품 ID"),
                                PayloadDocumentation.fieldWithPath("rating").description("별점"),
                                PayloadDocumentation.fieldWithPath("content").description("리뷰 내용")
                        )
                ))
                .andDo(MockMvcResultHandlers.print());

    }
}