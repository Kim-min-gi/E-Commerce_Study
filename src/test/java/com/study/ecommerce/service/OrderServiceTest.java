package com.study.ecommerce.service;

import com.study.ecommerce.domain.*;
import com.study.ecommerce.domain.Order;
import com.study.ecommerce.domain.type.OrderStatus;
import com.study.ecommerce.domain.type.Payment;
import com.study.ecommerce.exception.NotFoundOrderException;
import com.study.ecommerce.repository.*;
import com.study.ecommerce.request.OrderItemRequest;
import com.study.ecommerce.request.OrderRequest;
import com.study.ecommerce.response.OrderResponse;
import com.study.ecommerce.response.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@ActiveProfiles("test")
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
    void setUp() {
        cleanDatabase();
    }

    @AfterEach
    void tearDown() {
        cleanDatabase();
    }

    void cleanDatabase() {
        orderProductRepository.deleteAll();
        orderRepository.deleteAll();
        cartRepository.deleteAll();
        productRepository.deleteAll();
        productCategoryRepository.deleteAll();
        memberRepository.deleteAll();
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

        List<Product> requestProduct = IntStream.range(1,10).mapToObj(i ->
                Product.builder()
                        .name("물품" + i)
                        .quantity(i)
                        .price(i)
                        .build()
        ).toList();

        requestProduct.forEach(product -> product.setCategory(productCategory));

        productRepository.saveAll(requestProduct);


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
    @DisplayName("주문내역 한건 조회")
    public void testFindOrder() throws IllegalAccessException {

        // given
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

        Product product = Product.builder()
                .name("상품1")
                .price(1000)
                .quantity(100)
                .build();

        product.setCategory(productCategory);

        productRepository.save(product);

        List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("Testing@naver.com", null, authorities)
        );


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

        OrderResponse findOrder = orderService.findOrder(order.getId());

        Assertions.assertEquals("상품1",findOrder.getOrderProductResponse().get(0).getName());
        Assertions.assertEquals(1000,findOrder.getTotalPrice());
        Assertions.assertEquals(Payment.CARD,findOrder.getPayment());


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

        productRepository.saveAll(requestProduct);


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
        LocalDate end = LocalDate.of(2025,11,30);

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
    public void test4() throws IllegalAccessException {
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

        orderService.createOrder(request);

        Pageable pageable = PageRequest.of(0,10);


        List<OrderResponse> allOrders = orderService.findAllOrders(pageable);
        OrderResponse order = allOrders.get(0);

        Assertions.assertEquals("물품1",order.getOrderProductResponse().get(0).getName());
        Assertions.assertEquals("물품2",order.getOrderProductResponse().get(1).getName());
        Assertions.assertEquals(Payment.CARD,order.getPayment());
        Assertions.assertEquals(2000L,order.getTotalPrice());

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

        productRepository.saveAll(requestProduct);


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