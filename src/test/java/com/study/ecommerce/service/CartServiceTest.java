package com.study.ecommerce.service;

import com.study.ecommerce.repository.CartRepository;
import com.study.ecommerce.repository.MemberRepository;
import com.study.ecommerce.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CartServiceTest {

    private CartService cartService;

    private CartRepository cartRepository;

    private MemberRepository memberRepository;

    private ProductRepository productRepository;


    @BeforeEach
    void clean(){
        cartRepository.deleteAll();
        productRepository.deleteAll();
        memberRepository.deleteAll();
    }

}