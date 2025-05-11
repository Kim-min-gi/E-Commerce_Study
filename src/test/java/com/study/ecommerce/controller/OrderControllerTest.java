package com.study.ecommerce.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.ecommerce.config.CustomMockMember;
import com.study.ecommerce.domain.*;
import com.study.ecommerce.domain.Order;
import com.study.ecommerce.domain.type.OrderStatus;
import com.study.ecommerce.domain.type.Payment;
import com.study.ecommerce.repository.MemberRepository;
import com.study.ecommerce.repository.OrderRepository;
import com.study.ecommerce.repository.ProductCategoryRepository;
import com.study.ecommerce.repository.ProductRepository;
import com.study.ecommerce.request.OrderItemRequest;
import com.study.ecommerce.request.OrderRequest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;


import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;


@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme = "https",uriHost = "api.ecommerce.com", uriPort = 433)
@ExtendWith(RestDocumentationExtension.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

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
        productCategoryRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("주문 조회")
    @CustomMockMember
    void findAllOrders() throws Exception {
        //given
        Member member = Member.builder()
                .email("Testing@naver.com")
                .name("Test")
                .password("ASDA515184424")
                .role("ROLE_USER")
                .build();

        memberRepository.save(member);

        ProductCategory productCategory = ProductCategory.builder()
                .name("카테고리1")
                .build();

        productCategoryRepository.save(productCategory);

        List<Product> requestProduct = IntStream.range(1,31).mapToObj(i ->
                Product.builder()
                        .name("물품" + i)
                        .quantity(i)
                        .price(i)
                        .build()
        ).toList();

        requestProduct.forEach(product -> product.setCategory(productCategory));

        productCategoryRepository.save(productCategory);



        List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("Testing@naver.com", null, authorities)
        );


        Address address = Address.builder()
                .city("서울")
                .street("강남")
                .zipcode("1111")
                .build();

        List<Order> requestOrders = IntStream.range(1,31).mapToObj(i ->
                Order.builder()
                        .orderStatus(OrderStatus.ORDER_COMPLETE)
                        .address(address)
                        .payment(Payment.CARD)
                        .member(member)
                        .totalPrice(i * 100)
                        .build()
        ).toList();

        orderRepository.saveAll(requestOrders);


        mockMvc.perform(RestDocumentationRequestBuilders.get("/orders?page=1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(document("order/orderList",
                        responseFields(
                                fieldWithPath("[]").description("주문 리스트 배열"),
                                fieldWithPath("[].id").description("주문 번호"),
                                fieldWithPath("[].totalPrice").description("총 액수"),
                                fieldWithPath("[].orderProductResponse").description("상품"),
                                fieldWithPath("[].payment").description("결제 방법"),
                                fieldWithPath("[].address").description("배송 주소"),
                                fieldWithPath("[].address.city").description("도시"),
                                fieldWithPath("[].address.street").description("거리"),
                                fieldWithPath("[].address.zipcode").description("우편번호"),
                                fieldWithPath("[].orderDate").description("주문 날짜"),
                                fieldWithPath("[].orderStatus").description("주문 상태")
                        )
                ))
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    @DisplayName("주문내역 날짜로 조회")
    void findOrderDate() throws Exception {
        //given
        Member member = Member.builder()
                .email("Testing@naver.com")
                .name("Test")
                .password("ASDA515184424")
                .role("ROLE_USER")
                .build();

        memberRepository.save(member);

        ProductCategory productCategory = ProductCategory.builder()
                .name("카테고리1")
                .build();

        productCategoryRepository.save(productCategory);

        List<Product> requestProduct = IntStream.range(1,31).mapToObj(i ->
                Product.builder()
                        .name("물품" + i)
                        .quantity(i)
                        .price(i)
                        .build()
        ).toList();

        requestProduct.forEach(product -> product.setCategory(productCategory));

        productCategoryRepository.save(productCategory);


        List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("Testing@naver.com", null, authorities)
        );


        Address address = Address.builder()
                .city("서울")
                .street("강남")
                .zipcode("1111")
                .build();

        List<Order> requestOrders = IntStream.range(1,31).mapToObj(i ->
                Order.builder()
                        .orderStatus(OrderStatus.ORDER_COMPLETE)
                        .address(address)
                        .payment(Payment.CARD)
                        .member(member)
                        .totalPrice(i * 100)
                        .build()
        ).toList();


        orderRepository.saveAll(requestOrders);

        LocalDate start = LocalDate.of(2024,11,1);
        LocalDate end = LocalDate.of(2025,11,30);



        mockMvc.perform(RestDocumentationRequestBuilders.get("/ordersDate")
                                .param("page","1")
                                .param("startDate",start.toString())
                                .param("endDate",end.toString())
                //.content(objectMapper.writeValueAsString(start))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(document("order/orderDateList",
                        responseFields(
                                fieldWithPath("[]").description("주문 리스트 배열"),
                                fieldWithPath("[].id").description("주문 번호"),
                                fieldWithPath("[].totalPrice").description("총주문 금액"),
                                fieldWithPath("[].orderStatus").description("주문 상태"),
                                fieldWithPath("[].orderProductResponse").description("주문 상품들"),
                                fieldWithPath("[].payment").description("결제방법"),
                                fieldWithPath("[].address").description("배송 주소"),
                                fieldWithPath("[].address.city").description("도시"),
                                fieldWithPath("[].address.street").description("거리"),
                                fieldWithPath("[].address.zipcode").description("우편번호"),
                                fieldWithPath("[].orderDate").description("주문 날짜")
                        )
                ))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("주문 취소")
    void orderCancel() throws Exception {
        //given
        Member member = Member.builder()
                .email("Testing@naver.com")
                .name("Test")
                .password("ASDA515184424")
                .role("ROLE_USER")
                .build();

        memberRepository.save(member);

        ProductCategory productCategory = ProductCategory.builder()
                .name("카테고리1")
                .build();

        productCategoryRepository.save(productCategory);

        List<Product> requestProduct = IntStream.range(1,31).mapToObj(i ->
                Product.builder()
                        .name("물품" + i)
                        .quantity(i)
                        .price(i)
                        .build()
        ).toList();

        requestProduct.forEach(product -> product.setCategory(productCategory));

        productCategoryRepository.save(productCategory);


        List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("Testing@naver.com", null, authorities)
        );


        Address address = Address.builder()
                .city("서울")
                .street("강남")
                .zipcode("1111")
                .build();

        Order requestOrder =
                Order.builder()
                        .orderStatus(OrderStatus.ORDER_COMPLETE)
                        .address(address)
                        .payment(Payment.CARD)
                        .member(member)
                        .totalPrice(100)
                        .build();


        orderRepository.save(requestOrder);


        mockMvc.perform(RestDocumentationRequestBuilders.patch("/orderCancel/{orderId}",requestOrder.getId())
//               .content(objectMapper.writeValueAsString(requestOrder.getId()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(MockMvcRestDocumentation.document("order/orderCancel",
                        RequestDocumentation.pathParameters(
                                RequestDocumentation.parameterWithName("orderId").description("주문 번호")
                        )))
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    @DisplayName("주문 생성")
    void createOrder() throws Exception {
        //given
        Member member = Member.builder()
                .email("Testing@naver.com")
                .name("Test")
                .password("ASDA515184424")
                .role("ROLE_USER")
                .build();

        memberRepository.save(member);

        ProductCategory productCategory = ProductCategory.builder()
                .name("카테고리1")
                .build();

        productCategoryRepository.save(productCategory);

        List<Product> requestProduct = IntStream.range(1,10).mapToObj(i ->
                Product.builder()
                        .name("물품" + i)
                        .quantity(i)
                        .price(1000)
                        .build()
        ).toList();

        requestProduct.forEach(product -> product.setCategory(productCategory));

        productRepository.saveAll(requestProduct);


        List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("Testing@naver.com", null, authorities)
        );


        Address address = Address.builder()
                .city("서울")
                .street("강남")
                .zipcode("1111")
                .build();

        OrderItemRequest orderItemRequest = OrderItemRequest.builder()
                .productId(requestProduct.get(0).getId())
                .price(1000)
                .quantity(1)
                .build();

        OrderItemRequest orderItemRequest2 = OrderItemRequest.builder()
                .productId(requestProduct.get(1).getId())
                .price(1000)
                .quantity(1)
                .build();

        List<OrderItemRequest> orderItemRequestList = new ArrayList<>();

        orderItemRequestList.add(orderItemRequest);
        orderItemRequestList.add(orderItemRequest2);

        OrderRequest request = OrderRequest.builder()
                .memberId(member.getId())
                .payment(Payment.CARD)
                .address(address)
                .orderItemRequestList(orderItemRequestList)
                .totalPrice(2000L)
                .build();


        mockMvc.perform(RestDocumentationRequestBuilders.post("/orderCreate")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(MockMvcRestDocumentation.document("order/orderCreate",
                        requestFields(
                                fieldWithPath("orderId").description("주문 번호"),
                                fieldWithPath("memberId").description("주문회원"),
                                fieldWithPath("orderItemRequestList").description("주문 상품 List"),
                                fieldWithPath("orderItemRequestList[].productId").description("상품 ID"),
                                fieldWithPath("orderItemRequestList[].quantity").description("상품 수량"),
                                fieldWithPath("orderItemRequestList[].price").description("상품 가격"),
                                fieldWithPath("totalPrice").description("주문 총금액"),
                                fieldWithPath("payment").description("주문 결제 방법"),
                                fieldWithPath("address").description("주소"),
                                fieldWithPath("address.city").description("도시"),
                                fieldWithPath("address.street").description("거리"),
                                fieldWithPath("address.zipcode").description("우편번호"),
                                fieldWithPath("orderStatus").description("주문 상태"),
                                fieldWithPath("itemName").description("상품 결제용 이름")
                        )))
                .andDo(MockMvcResultHandlers.print());




    }

    @Test
    @DisplayName("주문 상태 변경")
    void modifyOrder() throws Exception {
        //given
        Member member = Member.builder()
                .email("Testing@naver.com")
                .name("Test")
                .password("ASDA515184424")
                .role("ROLE_ADMIN")
                .build();

        memberRepository.save(member);

        ProductCategory productCategory = ProductCategory.builder()
                .name("카테고리1")
                .build();

        productCategoryRepository.save(productCategory);

        List<Product> requestProduct = IntStream.range(1,31).mapToObj(i ->
                Product.builder()
                        .name("물품" + i)
                        .quantity(i)
                        .price(i)
                        .build()
        ).toList();

        requestProduct.forEach(product -> product.setCategory(productCategory));

        productCategoryRepository.save(productCategory);


        List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"));
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("Testing@naver.com", null, authorities)
        );


        Address address = Address.builder()
                .city("서울")
                .street("강남")
                .zipcode("1111")
                .build();

        Order requestOrder =
                Order.builder()
                        .orderStatus(OrderStatus.ORDER_COMPLETE)
                        .address(address)
                        .payment(Payment.CARD)
                        .member(member)
                        .totalPrice(100)
                        .build();


        orderRepository.save(requestOrder);

        OrderRequest orderRequest = OrderRequest.builder()
                .orderId(requestOrder.getId())
                .orderStatus(OrderStatus.PROCESSING)
                .build();


        mockMvc.perform(RestDocumentationRequestBuilders.patch("/orderModify")
                .content(objectMapper.writeValueAsString(orderRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(MockMvcRestDocumentation.document("order/orderModify",
                        requestFields(
                                fieldWithPath("orderId").description("주문 번호"),
                                fieldWithPath("memberId").description("주문회원"),
                                fieldWithPath("orderItemRequestList").description("주문 상품 List"),
                                fieldWithPath("totalPrice").description("주문 총금액"),
                                fieldWithPath("payment").description("주문 결제 방법"),
                                fieldWithPath("address").description("주소"),
                                fieldWithPath("orderStatus").description("주문 상태"),
                                fieldWithPath("itemName").description("상품 결제용 이름")
                        )))
                .andDo(MockMvcResultHandlers.print());
    }
}