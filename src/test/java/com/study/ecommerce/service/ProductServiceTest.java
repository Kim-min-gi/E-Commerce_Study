package com.study.ecommerce.service;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.study.ecommerce.domain.Product;
import com.study.ecommerce.domain.ProductCategory;
import com.study.ecommerce.repository.ProductCategoryRepository;
import com.study.ecommerce.repository.ProductRepository;
import com.study.ecommerce.request.ProductRequest;
import com.study.ecommerce.response.ProductResponse;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.boot.convert.DataSizeUnit;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

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
        productCategoryRepository.deleteAll();
    }


    @Test
    @DisplayName("상품 추가")
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
        Assertions.assertEquals(product.getName(), "물품1");
        Assertions.assertEquals(product.getAmount(),99);

    }


    @Test
    @DisplayName("상품 한개 조회")
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

        Optional<Product> product = productRepository.findByName("물품1");

        Optional<Product> product2 = productService.getProduct(product.get().getId());

        Assertions.assertEquals(product2.get().getName(),"물품1");
        Assertions.assertEquals(product2.get().getAmount(),99);

    }


    @Test
    @DisplayName("상품 여러개 조회")
    void test3(){

        ProductCategory productCategory = ProductCategory.builder()
                .name("카테고리1")
                .build();

        productCategoryRepository.save(productCategory);


        List<Product> requestProduct = IntStream.range(1,31).mapToObj(i -> Product.builder()
                .name("물품" + i)
                .amount(i)
                .price(i)
                .productCategory(productCategory)
                .build()).toList();

        productRepository.saveAll(requestProduct);

        Pageable pageable = PageRequest.of(0,10);


        List<ProductResponse> productResponses = productService.getProducts(pageable);

        Assertions.assertEquals(10,productResponses.size());
        Assertions.assertEquals("물품1",productResponses.get(0).getName());

    }


    @Test
    @DisplayName("상품 수정")
    void test4() throws Exception {

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

        ProductRequest productRequest2 = ProductRequest.builder()
                .name("물품2")
                .price(16100)
                .productCategory(productCategory)
                .amount(9)
                .build();

        product.modifyProduct(productRequest2);


        Assertions.assertEquals(product.getName(), productRequest2.getName());
        Assertions.assertEquals(product.getPrice(), productRequest2.getPrice());
        Assertions.assertEquals(product.getAmount(), productRequest2.getAmount());
        Assertions.assertEquals(product.getProductCategory(), productRequest2.getProductCategory());

    }

    @Test
    @DisplayName("상품 삭제")
    void test5() throws Exception{

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

        productService.removeProduct(product.getId());


        //Assertions.assertThrows() 변경??
        Assertions.assertEquals(0,productRepository.findAll().size());

    }







}