package com.study.ecommerce.controller;

import com.study.ecommerce.repository.ProductCategoryRepository;
import com.study.ecommerce.service.ProductCategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProductCategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductCategoryService productCategoryService;

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @BeforeEach
    void clean(){
        productCategoryRepository.deleteAll();
    }

    @Test
    void addCategory() {

    }

    @Test
    void modifyCategory() {

    }

    @Test
    void removeCategory() {

    }
}