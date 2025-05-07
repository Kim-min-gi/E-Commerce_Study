package com.study.ecommerce.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.ecommerce.config.CustomMockMember;
import com.study.ecommerce.domain.Product;
import com.study.ecommerce.domain.ProductCategory;
import com.study.ecommerce.repository.ProductCategoryRepository;
import com.study.ecommerce.repository.ProductRepository;
import com.study.ecommerce.request.ProductRequest;
import com.study.ecommerce.service.ProductService;
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
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.stream.IntStream;

import static org.springframework.restdocs.payload.PayloadDocumentation.*;


@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme = "https",uriHost = "api.ecommerce.com", uriPort = 433)
@ExtendWith(RestDocumentationExtension.class)
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
    void setUp() {
        cleanDatabase();
    }

    @AfterEach
    void tearDown() {
        cleanDatabase();
    }


    void cleanDatabase() {
        productRepository.deleteAll();
        productCategoryRepository.deleteAll();
    }



    @Test
    @DisplayName("상품 추가 (관리자)")
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
                .quantity(99)
                .build();



        mockMvc.perform(RestDocumentationRequestBuilders.post("/admin/product")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productRequest)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(MockMvcRestDocumentation.document("product/productAdd",
                        requestFields(
                                fieldWithPath("name").description("상품명"),
                                fieldWithPath("price").description("상품 가격"),
                                fieldWithPath("quantity").description("상품 수"),
                                fieldWithPath("categoryName").description("카테고리명")
                        )
                        ))
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
                .quantity(i)
                .price(i)
                .build()).toList();

        requestProduct.forEach(product -> product.setCategory(productCategory));

        productRepository.saveAll(requestProduct);


        mockMvc.perform(RestDocumentationRequestBuilders.get("/product/products?page=1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcRestDocumentation.document("product/product-list",
                        responseFields(
                                fieldWithPath("[]").description("상품 리스트 배열"),
                                fieldWithPath("[].id").description("상품 번호"),
                                fieldWithPath("[].name").description("상품 이름"),
                                fieldWithPath("[].price").description("상품 개당 가격"),
                                fieldWithPath("[].quantity").description("상품 수량"),
                                fieldWithPath("[].categoryName").description("상품 카테고리 명")
                        )
                ))
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    @DisplayName("상품 한개 조회 (관리자)")
    @CustomMockMember
    void getProduct() throws Exception{
        ProductCategory productCategory = ProductCategory.builder()
                .name("카테고리1")
                .build();

        productCategoryRepository.save(productCategory);

        ProductRequest productRequest = ProductRequest.builder()
                .name("물품1")
                .price(161000)
                .categoryName(productCategory.getName())
                .quantity(99)
                .build();

        productService.addProduct(productRequest);

        Product productId = productRepository.findByName("물품1").get();



        mockMvc.perform(RestDocumentationRequestBuilders.get("/admin/product/{productId}",productId.getId())
//                        .content(objectMapper.writeValueAsString(idRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("물품1"))
                .andDo(MockMvcRestDocumentation.document("product/getProduct",
                        RequestDocumentation.pathParameters(
                                RequestDocumentation.parameterWithName("productId").description("상품 번호")
                        ),
                        PayloadDocumentation.responseFields(
                                fieldWithPath("id").description("상품 번호"),
                                fieldWithPath("name").description("상품 이름"),
                                fieldWithPath("price").description("상품 개당 가격"),
                                fieldWithPath("quantity").description("상품 수량"),
                                fieldWithPath("categoryName").description("상품 카테고리 명")
                        )))
                .andDo(MockMvcResultHandlers.print());


    }

    @Test
    @DisplayName("상품 수정 (관리자)")
    @CustomMockMember
    void modifyProduct() throws Exception {
        ProductCategory productCategory = ProductCategory.builder()
                .name("카테고리1")
                .build();

        productCategoryRepository.save(productCategory);


        List<Product> requestProduct = IntStream.range(1,31).mapToObj(i -> Product.builder()
                .name("물품" + i)
                .quantity(i)
                .price(i)
                .build()).toList();

        requestProduct.forEach(product -> product.setCategory(productCategory));

        productRepository.saveAll(requestProduct);

        ProductRequest productRequest = ProductRequest.builder()
                .categoryName(productCategory.getName())
                .name("바뀐물품1")
                .quantity(123)
                .price(1211)
                        .build();



        mockMvc.perform(RestDocumentationRequestBuilders.patch("/admin/product/{productId}",requestProduct.get(0).getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRequest)))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(MockMvcRestDocumentation.document("product/modifyProduct",
                        RequestDocumentation.pathParameters(
                                RequestDocumentation.parameterWithName("productId").description("상품 번호")
                        )))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("상품 삭제 (관리자)")
    @CustomMockMember
    void removeProduct() throws Exception{
        ProductCategory productCategory = ProductCategory.builder()
                .name("카테고리1")
                .build();

        productCategoryRepository.save(productCategory);

        ProductRequest productRequest = ProductRequest.builder()
                .name("물품1")
                .price(161000)
                .categoryName(productCategory.getName())
                .quantity(99)
                .build();

        productService.addProduct(productRequest);

        Product product = productRepository.findByName("물품1").get();


        mockMvc.perform(RestDocumentationRequestBuilders.delete("/admin/product/{productId}",product.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(MockMvcRestDocumentation.document("product/deleteProduct",
                        RequestDocumentation.pathParameters(
                                RequestDocumentation.parameterWithName("productId").description("상품 번호")
                        )))
                .andDo(MockMvcResultHandlers.print());

    }




//    @Test
//    @DisplayName("상품 상세 내용")
//    @CustomMockMember
//    void getProductDetails() throws Exception{
//        ProductCategory productCategory = ProductCategory.builder()
//                .name("카테고리1")
//                .build();
//
//        productCategoryRepository.save(productCategory);
//
//        ProductRequest productRequest = ProductRequest.builder()
//                .name("물품1")
//                .price(161000)
//                .categoryName(productCategory.getName())
//                .quantity(99)
//                .build();
//
//        productService.addProduct(productRequest);
//
//        Product product = productRepository.findByName("물품1").get();
//    }





    @Test
    @DisplayName("카테고리 상품 리스트 조회")
    @CustomMockMember
    void categoryProductList() throws Exception{
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
                        .quantity(i)
                        .price(i)
                        .build()
        ).toList();


        requestProduct.forEach(product -> product.setCategory(productCategory));

        productCategoryRepository.save(productCategory);



        mockMvc.perform(RestDocumentationRequestBuilders.get("/product/list/{categoryId}",productCategory.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("물품1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[29].name").value("물품30"))
                .andDo(MockMvcRestDocumentation.document("product/category-list",
                        RequestDocumentation.pathParameters(
                                RequestDocumentation.parameterWithName("categoryId").description("카테고리 번호")
                        )))
                .andDo(MockMvcResultHandlers.print());

    }



}