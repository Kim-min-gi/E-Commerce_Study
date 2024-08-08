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

    @AfterEach
    void cleanup(){
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
                .categoryName(productCategory.getName())
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
                .categoryName(productCategory.getName())
                .amount(99)
                .build();

        productService.addProduct(productRequest);

        Product product = productRepository.findByName("물품1").get();

        ProductResponse product2 = productService.getProduct(product.getId());

        Assertions.assertEquals(product2.getName(),"물품1");
        Assertions.assertEquals(product2.getAmount(),99);

    }


    @Test
    @DisplayName("상품 여러개 조회")
    void test3(){

        ProductCategory productCategory = ProductCategory.builder()
                .name("카테고리1")
                .build();


        List<Product> requestProduct = IntStream.range(1,31).mapToObj(i ->
                Product.builder()
                .name("물품" + i)
                .amount(i)
                .price(i)
                .build()
        ).toList();

        requestProduct.forEach(product -> product.setCategory(productCategory));

        productCategoryRepository.save(productCategory);

        Pageable pageable = PageRequest.of(0,10);


        List<ProductResponse> productResponses = productService.getProducts(pageable);

        Assertions.assertEquals(10,productResponses.size());
        Assertions.assertEquals("물품1",productResponses.get(0).getName());

    }


    @Test
    @DisplayName("상품 수정")
    @Transactional
    void test4() throws Exception {

        ProductCategory productCategory = ProductCategory.builder()
                .name("카테고리1")
                .build();

        productCategoryRepository.save(productCategory);

        ProductCategory productCategory2 = ProductCategory.builder()
                .name("카테고리2")
                .build();

        productCategoryRepository.save(productCategory2);

        ProductRequest productRequest = ProductRequest.builder()
                .name("물품1")
                .price(161000)
                .amount(99)
                .categoryName(productCategory.getName())
                .build();


        productService.addProduct(productRequest);

        Product product = productRepository.findByName("물품1").get();

        ProductRequest productRequest2 = ProductRequest.builder()
                .name("바뀐물품1")
                .price(220)
                .categoryName(productCategory2.getName())
                .amount(9)
                .build();

        productService.modifyProduct(product.getId(),productRequest2);

        Product product2 = productRepository.findByName("바뀐물품1").get();




        Assertions.assertEquals(product2.getId(), product.getId());
        Assertions.assertEquals(product2.getName(), productRequest2.getName());
        Assertions.assertEquals(product2.getPrice(), productRequest2.getPrice());
        Assertions.assertEquals(product2.getAmount(), productRequest2.getAmount());
        Assertions.assertEquals(product2.getProductCategory().getName(), productRequest2.getCategoryName());

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
                .categoryName(productCategory.getName())
                .amount(99)
                .build();

        productService.addProduct(productRequest);


        Product product = productRepository.findByName("물품1").get();

        productService.removeProduct(product.getId());


        //Assertions.assertThrows() 변경??
        Assertions.assertEquals(0,productRepository.findAll().size());

    }

    @Test
    @DisplayName("카테고리별 상품 조회")
    void test6() throws Exception{

        ProductCategory productCategory = ProductCategory.builder()
                .name("카테고리1")
                .build();

        productCategoryRepository.save(productCategory);

        ProductCategory productCategory2 = ProductCategory.builder()
                .name("카테고리2")
                .build();

        productCategoryRepository.save(productCategory2);

        List<Product> requestProduct = IntStream.range(1,31).mapToObj(i ->
                Product.builder()
                        .name("물품" + i)
                        .amount(i)
                        .price(i)
                        .build()
        ).toList();


        requestProduct.forEach(product -> product.setCategory(productCategory));

        productCategoryRepository.save(productCategory);



        List<ProductResponse> productResponses = productService.getCategoryProduct(productCategory.getId());

        Assertions.assertEquals("물품1",productResponses.get(0).getName());
        Assertions.assertEquals("물품2",productResponses.get(1).getName());
        Assertions.assertEquals("물품3",productResponses.get(2).getName());

    }





}