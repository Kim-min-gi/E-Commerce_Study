package com.study.ecommerce.service;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.study.ecommerce.domain.Product;
import com.study.ecommerce.domain.ProductCategory;
import com.study.ecommerce.repository.ProductCategoryRepository;
import com.study.ecommerce.repository.ProductRepository;
import com.study.ecommerce.request.ProductRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.convert.DataSizeUnit;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @BeforeEach
    void clean(){
        productRepository.deleteAll();
    }


    @Test
    @DisplayName("상품 추가")
    @Transactional
    void test1() {

        ProductCategory productCategory = ProductCategory.builder()
                .name("카테고리1")
                .build();

        productCategoryRepository.save(productCategory);


        ProductRequest productRequest = ProductRequest.builder()
                .name("물품1")
                .price(161000)
                .productCategory(productCategory)
                .amount(99)
                .build();

        productService.addProduct(productRequest);

        Product product = productRepository.findByName("물품1").get();

        Assertions.assertEquals(productCategory.getId(),product.getProductCategory().getId());
        Assertions.assertEquals(product.getId(),productCategory.getProduct().get(0).getId());
        Assertions.assertEquals(product.getName(), "물품1");
        Assertions.assertEquals(product.getAmount(),99);

    }


    @Test
    @DisplayName("상품 한개 조회")
    @Transactional
    void test2() {

        ProductCategory productCategory = ProductCategory.builder()
                .name("카테고리1")
                .build();

        productCategoryRepository.save(productCategory);


        ProductRequest productRequest = ProductRequest.builder()
                .name("물품1")
                .price(161000)
                .productCategory(productCategory)
                .amount(99)
                .build();

        productService.addProduct(productRequest);


        Optional<Product> product = productService.getProduct(1L);

        Assertions.assertEquals(product.get().getName(),"물품1");
        Assertions.assertEquals(product.get().getAmount(),99);

    }


    @Test
    @DisplayName("상품 여러개 조화")
    @Transactional
    void test3(){

        ProductCategory productCategory = ProductCategory.builder()
                .name("카테고리1")
                .build();

        productCategoryRepository.save(productCategory);


        ProductRequest productRequest = ProductRequest.builder()
                .name("물품1")
                .price(161000)
                .productCategory(productCategory)
                .amount(99)
                .build();

        productService.addProduct(productRequest);

        ProductRequest productRequest2 = ProductRequest.builder()
                .name("물품2")
                .price(16100)
                .productCategory(productCategory)
                .amount(9)
                .build();

        productService.addProduct(productRequest2);

        List<Product> products = productRepository.findAll();


        Assertions.assertEquals(2,products.size());
        Assertions.assertEquals(productRequest.getName(),products.get(0).getName());
        Assertions.assertEquals(productRequest2.getName(),products.get(1).getName());

    }








}