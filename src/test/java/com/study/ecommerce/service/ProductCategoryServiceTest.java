package com.study.ecommerce.service;

import com.study.ecommerce.domain.ProductCategory;
import com.study.ecommerce.repository.ProductCategoryRepository;
import com.study.ecommerce.request.CategoryRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductCategoryServiceTest {

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Autowired
    private ProductCategoryService productCategoryService;

    @Test
    @DisplayName("카테고리 생성")
    void tset1() {

        CategoryRequest categoryRequest = CategoryRequest.builder()
                .name("카테고리1")
                .build();


        productCategoryService.addCategory(categoryRequest);

    }





}