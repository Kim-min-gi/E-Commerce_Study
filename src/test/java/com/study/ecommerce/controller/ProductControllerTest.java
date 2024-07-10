package com.study.ecommerce.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.ecommerce.config.CustomMockMember;
import com.study.ecommerce.domain.Product;
import com.study.ecommerce.domain.ProductCategory;
import com.study.ecommerce.repository.ProductCategoryRepository;
import com.study.ecommerce.repository.ProductRepository;
import com.study.ecommerce.request.ProductRequest;
import com.study.ecommerce.service.ProductService;
import jakarta.persistence.Access;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void clean(){
        productRepository.deleteAll();
        productCategoryRepository.deleteAll();
    }

    @Test
    @DisplayName("상품 추가")
    @CustomMockMember
    void addProduct() throws Exception{

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



        mockMvc.perform(MockMvcRequestBuilders.post("/admin/product")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productRequest)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(MockMvcResultHandlers.print());


    }

    @Test
    @DisplayName("상품 리스트 조회")
    @CustomMockMember
    void getProducts() throws Exception{
        ProductCategory productCategory = ProductCategory.builder()
                .name("카테고리1")
                .build();

        productCategoryRepository.save(productCategory);


        List<Product> requestProduct = IntStream.range(1,31).mapToObj(i -> Product.builder()
                .name("물품" + i)
                .amount(i)
                .price(i)
                .build()).toList();

        requestProduct.forEach(product -> product.setCategory(productCategory));

        productRepository.saveAll(requestProduct);


        //페이징 테스트


        mockMvc.perform(MockMvcRequestBuilders.get("/admin/products?page=1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    @DisplayName("상품 한개 조회")
    @CustomMockMember
    void getProduct() throws Exception{
        ProductCategory productCategory = ProductCategory.builder()
                .name("카테고리1")
                .build();

        productCategoryRepository.save(productCategory);


        List<Product> requestProduct = IntStream.range(1,31).mapToObj(i -> Product.builder()
                .name("물품" + i)
                .amount(i)
                .price(i)
                .build()).toList();

        requestProduct.forEach(product -> product.setCategory(productCategory));

        productRepository.saveAll(requestProduct);



        mockMvc.perform(MockMvcRequestBuilders.get("/admin/product/{id}",1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("물품1"))
                .andDo(MockMvcResultHandlers.print());


    }

    @Test
    @DisplayName("상품 수정")
    @CustomMockMember
    void modifyProduct() throws Exception {
        ProductCategory productCategory = ProductCategory.builder()
                .name("카테고리1")
                .build();

        productCategoryRepository.save(productCategory);


        List<Product> requestProduct = IntStream.range(1,31).mapToObj(i -> Product.builder()
                .name("물품" + i)
                .amount(i)
                .price(i)
                .build()).toList();

        requestProduct.forEach(product -> product.setCategory(productCategory));

        productRepository.saveAll(requestProduct);

        ProductRequest productRequest = ProductRequest.builder()
                .categoryName(productCategory.getName())
                .name("바뀐물품1")
                .amount(123)
                .price(1211)
                        .build();



        mockMvc.perform(MockMvcRequestBuilders.patch("/admin/product/{id}",1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRequest)))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("상품 삭제")
    @CustomMockMember
    void removeProduct() throws Exception{
        ProductCategory productCategory = ProductCategory.builder()
                .name("카테고리1")
                .build();

        productCategoryRepository.save(productCategory);


        List<Product> requestProduct = IntStream.range(1,31).mapToObj(i -> Product.builder()
                .name("물품" + i)
                .amount(i)
                .price(i)
                .build()).toList();

        requestProduct.forEach(product -> product.setCategory(productCategory));

        productRepository.saveAll(requestProduct);


        mockMvc.perform(MockMvcRequestBuilders.delete("/admin/product/{id}",1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(MockMvcResultHandlers.print());

    }



}