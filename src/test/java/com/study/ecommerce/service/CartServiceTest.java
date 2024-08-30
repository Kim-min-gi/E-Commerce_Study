package com.study.ecommerce.service;

import com.study.ecommerce.domain.Cart;
import com.study.ecommerce.domain.Member;
import com.study.ecommerce.domain.Product;
import com.study.ecommerce.domain.ProductCategory;
import com.study.ecommerce.repository.CartRepository;
import com.study.ecommerce.repository.MemberRepository;
import com.study.ecommerce.repository.ProductCategoryRepository;
import com.study.ecommerce.repository.ProductRepository;
import com.study.ecommerce.response.CartListResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CartServiceTest {

    @Autowired
    private CartService cartService;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Autowired
    private ProductRepository productRepository;


    @BeforeEach
    void clean(){
        productCategoryRepository.deleteAll();
        cartRepository.deleteAll();
        productRepository.deleteAll();
        memberRepository.deleteAll();
    }


    @Test
    @DisplayName("장바구니에 상품 추가")
    public void test1(){

    }

    @Test
    @DisplayName("장바구니에 상품 삭제")
    public void test2(){

    }

    @Test
    @DisplayName("장바구니에 상품 수량 변경")
    public void test3(){

    }

    @Test
    @DisplayName("장바구니에 상품리스트 출력")
    public void test4(){
        //given
        Member member = Member.builder()
                .email("Testing@naver.com")
                .name("Test")
                .role("ROLE_USER")
                .password("ASDA515184424")
                .build();

        memberRepository.save(member);

        ProductCategory productCategory = ProductCategory.builder()
                .name("카테고리1")
                .build();

        productCategoryRepository.save(productCategory);

        Product product = Product.builder()
                .name("물품1")
                .price(1000)
                .quantity(10)
                .build();

        Product product2 = Product.builder()
                .name("물품2")
                .price(51)
                .quantity(100)
                .build();

        product.setCategory(productCategory);
        product2.setCategory(productCategory);

        productRepository.save(product);
        productRepository.save(product2);



        Cart cart = Cart.createCart(member, product, 3);
        Cart cart2 = Cart.createCart(member, product2, 5);
        cartRepository.save(cart);
        cartRepository.save(cart2);

        List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("Testing@naver.com", null, authorities)
        );


        //when
        List<CartListResponse> listCart = cartService.getCartList();

        //then
        Assertions.assertEquals(listCart.size(),2);
        Assertions.assertEquals(listCart.get(0).getQuantity(),3);
        Assertions.assertEquals(listCart.get(0).getProductName(),"물품1");
        Assertions.assertEquals(listCart.get(1).getQuantity(),5);
        Assertions.assertEquals(listCart.get(1).getProductName(),"물품2");

    }


}