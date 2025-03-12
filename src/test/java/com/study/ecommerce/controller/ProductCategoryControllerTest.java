package com.study.ecommerce.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.ecommerce.config.CustomMockMember;
import com.study.ecommerce.domain.ProductCategory;
import com.study.ecommerce.repository.ProductCategoryRepository;
import com.study.ecommerce.request.CategoryRequest;
import com.study.ecommerce.service.ProductCategoryService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.objenesis.SpringObjenesis;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme = "https",uriHost = "api.ecommerce.com", uriPort = 433)
@ExtendWith(RestDocumentationExtension.class)
class ProductCategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductCategoryService productCategoryService;


    @Autowired
    private ProductCategoryRepository productCategoryRepository;


    @BeforeEach
    void setUp() {
        cleanDatabase();
    }

    @AfterEach
    void tearDown() {
        cleanDatabase();
    }


    void cleanDatabase() {
        productCategoryRepository.deleteAll();
    }


    @Test
    @DisplayName("카테고리 추가")
    @CustomMockMember
    void addCategory() throws Exception{

        CategoryRequest categoryRequest = CategoryRequest.builder()
                .name("카테고리1")
                .build();


        mockMvc.perform(RestDocumentationRequestBuilders.post("/admin/category")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryRequest)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(MockMvcRestDocumentation.document("category/addCategory",
                                PayloadDocumentation.requestFields(
                                        PayloadDocumentation.fieldWithPath("id").description("카테고리 아이디 (새로 생성이라 Null)"),
                                        PayloadDocumentation.fieldWithPath("name").description("카테고리 명")
                                ))
                        )
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    @DisplayName("카테고리 수정")
    @CustomMockMember
    void modifyCategory() throws Exception{

        CategoryRequest categoryRequest = CategoryRequest.builder()
                .name("카테고리1")
                .build();

        productCategoryRepository.save(ProductCategory.form(categoryRequest));

        ProductCategory productCategory = productCategoryRepository.findByName("카테고리1").get();

        CategoryRequest pathCategoryRequest = CategoryRequest.builder()
                .name("카테고리11233")
                .build();


        mockMvc.perform(RestDocumentationRequestBuilders.patch("/admin/category/{id}",productCategory.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pathCategoryRequest)))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(MockMvcRestDocumentation.document("category/modifyCategory",
                        RequestDocumentation.pathParameters(
                                RequestDocumentation.parameterWithName("id").description("카테고리 아이디")
                        )
                ))
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    @DisplayName("카테고리 삭제")
    @CustomMockMember
    void removeCategory() throws Exception {

        CategoryRequest categoryRequest = CategoryRequest.builder()
                .name("카테고리1")
                .build();

        productCategoryRepository.save(ProductCategory.form(categoryRequest));

        ProductCategory productCategory = productCategoryRepository.findByName("카테고리1").get();

        mockMvc.perform(RestDocumentationRequestBuilders.delete("/admin/category/{id}",productCategory.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(MockMvcRestDocumentation.document("category/deleteCategory",
                        RequestDocumentation.pathParameters(
                                RequestDocumentation.parameterWithName("id").description("카테고리 아이디")
                        )
                ))
                .andDo(MockMvcResultHandlers.print());

    }


    //카테고리 리스트 조회 및 한개 조회
}