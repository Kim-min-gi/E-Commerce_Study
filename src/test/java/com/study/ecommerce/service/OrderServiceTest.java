package com.study.ecommerce.service;

import com.study.ecommerce.domain.*;
import com.study.ecommerce.domain.type.OrderStatus;
import com.study.ecommerce.domain.type.Payment;
import com.study.ecommerce.repository.*;
import com.study.ecommerce.request.OrderRequest;
import com.study.ecommerce.response.OrderResponse;
import com.study.ecommerce.response.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;


@SpringBootTest
class OrderServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private OrderProductRepository orderProductRepository;

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderService orderService;

    @BeforeEach
    void clean(){
        memberRepository.deleteAll();
        orderRepository.deleteAll();
        cartRepository.deleteAll();
        orderProductRepository.deleteAll();
        productCategoryRepository.deleteAll();
        productRepository.deleteAll();
    }


    @Test
    @DisplayName("주문내역 조회")
        public void test1(){


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


        Pageable pageable = PageRequest.of(0,10);



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




        //when
        List<OrderResponse> orders =  orderService.findAllOrders(pageable);


        //then

        Assertions.assertEquals(100,orders.get(0).getTotalPrice());
        Assertions.assertEquals(Payment.CARD,orders.get(0).getPayment());
        Assertions.assertEquals(10,orders.size());


    }


    @Test
    @DisplayName("주문내역 날짜로 조회")
    public void test2(){

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


        Pageable pageable = PageRequest.of(0,10);



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
        LocalDate end = LocalDate.of(2024,11,30);

        //when
        List<OrderResponse> orders =  orderService.findOrderDate(pageable,start,end);

        //then

        Assertions.assertEquals(100,orders.get(0).getTotalPrice());

    }


    @Test
    @DisplayName("주문취소")
    public void test3(){

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

        orderService.orderCancel(requestOrder.getId());

        //then
        Order updatedOrder = orderRepository.findById(requestOrder.getId()).orElseThrow();
        Assertions.assertEquals(OrderStatus.CANCELED,updatedOrder.getOrderStatus());
    }


    @Test
    @DisplayName("주문생성")
    public void test4(){

    }

    @Test
    @DisplayName("관리자용 주문수정")
    public void test5() throws Exception {
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


        orderService.modifyOrder(orderRequest);

        //then
        Order updatedOrder = orderRepository.findById(requestOrder.getId()).orElseThrow();
        Assertions.assertEquals(OrderStatus.PROCESSING,updatedOrder.getOrderStatus());
    }


}