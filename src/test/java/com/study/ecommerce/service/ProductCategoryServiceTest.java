package com.study.ecommerce.service;

import com.study.ecommerce.domain.ProductCategory;
import com.study.ecommerce.repository.ProductCategoryRepository;
import com.study.ecommerce.request.CategoryRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductCategoryServiceTest {

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Autowired
    private ProductCategoryService productCategoryService;

    @BeforeEach
    void clean(){
        productCategoryRepository.deleteAll();
    }

    @Test
    @DisplayName("카테고리 생성")
    void tset1() {

        CategoryRequest categoryRequest = CategoryRequest.builder()
                .name("카테고리1")
                .build();

        CategoryRequest categoryRequest2 = CategoryRequest.builder()
                .name("카테고리2")
                .build();


        productCategoryService.addCategory(categoryRequest);
        productCategoryService.addCategory(categoryRequest2);


        List<ProductCategory> productCategories = productCategoryRepository.findAll();

        Assertions.assertEquals(2, productCategories.size());

    }



    @Test
    @DisplayName("카테고리 수정")
    void test2(){
        CategoryRequest categoryRequest = CategoryRequest.builder()
                .name("카테고리1")
                .build();

        productCategoryService.addCategory(categoryRequest);


        ProductCategory productCategory = productCategoryRepository.findByName("카테고리1").get();



        CategoryRequest categoryRequest2 = CategoryRequest.builder()
                .name("카테고리1121212")
                .build();

        productCategoryService.modifyCategory(productCategory.getId(),categoryRequest2);

        Optional<ProductCategory> findCategory = productCategoryRepository.findById(1L);



        Assertions.assertEquals(findCategory.get().getName(),categoryRequest2.getName());

    }

    @Test
    @DisplayName("카테고리 삭제")
    void test3(){
        CategoryRequest categoryRequest = CategoryRequest.builder()
                .name("카테고리1")
                .build();

        productCategoryService.addCategory(categoryRequest);

        Optional<ProductCategory> productCategory = productCategoryRepository.findByName("카테고리1");

        productCategoryRepository.delete(productCategory.get());

        Assertions.assertEquals(0,productCategoryRepository.findAll().size());


    }






}