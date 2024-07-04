package com.study.ecommerce.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.ecommerce.repository.ProductRepository;
import com.study.ecommerce.service.ProductService;
import jakarta.persistence.Access;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductControllerTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void clean(){
        productRepository.deleteAll();
    }

    @Test
    @DisplayName("상품 추가")
    void addProduct() throws Exception{

    }

    @Test
    @DisplayName("상품 리스트 조회")
    void getProducts() throws Exception{

    }

    @Test
    @DisplayName("상품 한개 조회")
    void getProduct() throws Exception{

    }

    @Test
    @DisplayName("상품 수정")
    void modifyProduct() throws Exception {

    }

    @Test
    @DisplayName("상품 삭제")
    void removeProduct() throws Exception{

    }



}