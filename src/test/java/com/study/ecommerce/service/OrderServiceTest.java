package com.study.ecommerce.service;

import com.study.ecommerce.domain.Member;
import com.study.ecommerce.domain.Product;
import com.study.ecommerce.domain.ProductCategory;
import com.study.ecommerce.repository.*;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

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

        //when
        orderService.findAllOrders(pageable);




        //then


//        Assertions.assertEquals(listCart.size(),1);
//        Assertions.assertEquals(listCart.get(0).getQuantity(),3);
//        Assertions.assertEquals(listCart.get(0).getProductName(),"물품1");


    }


    @Test
    @DisplayName("주문내역 날짜로 조회")
    public void test2(){

    }


    @Test
    @DisplayName("주문취소")
    public void test3(){

    }


    @Test
    @DisplayName("주문생성")
    public void test4(){

    }

    @Test
    @DisplayName("주문수정")
    public void test5(){

    }


}