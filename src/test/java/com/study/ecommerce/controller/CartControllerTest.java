package com.study.ecommerce.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.ecommerce.config.CustomMockMember;
import com.study.ecommerce.domain.Cart;
import com.study.ecommerce.domain.Member;
import com.study.ecommerce.domain.Product;
import com.study.ecommerce.domain.ProductCategory;
import com.study.ecommerce.repository.CartRepository;
import com.study.ecommerce.repository.MemberRepository;
import com.study.ecommerce.repository.ProductCategoryRepository;
import com.study.ecommerce.repository.ProductRepository;
import com.study.ecommerce.request.CartRequest;
import com.study.ecommerce.service.CartService;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme = "https",uriHost = "api.ecommerce.com", uriPort = 433)
@ExtendWith(RestDocumentationExtension.class)
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProductRepository productRepository;

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
        cartRepository.deleteAll();
        memberRepository.deleteAll();
        productRepository.deleteAll();
        productCategoryRepository.deleteAll();
    }

    @Test
    @DisplayName("카트 리스트 조회")
    @CustomMockMember(role = "ROLE_USER")
    void getCartList() throws Exception {
        //given
        Member member = Member.builder()
                .email("Testing@naver.com")
                .name("Test")
                .role("ROLE_USER")
                .password("ASDA515184424")
                .build();

        memberRepository.save(member);

        ProductCategory productCategory = ProductCategory.builder()
                .name("카테고리1")
                .build();

        productCategoryRepository.save(productCategory);

        Product product = Product.builder()
                .name("물품1")
                .price(1000)
                .quantity(10)
                .build();

        Product product2 = Product.builder()
                .name("물품2")
                .price(51)
                .quantity(100)
                .build();

        product.setCategory(productCategory);
        product2.setCategory(productCategory);

        productRepository.save(product);
        productRepository.save(product2);



        Cart cart = Cart.createCart(member, product, 3);
        Cart cart2 = Cart.createCart(member, product2, 5);
        cartRepository.save(cart);
        cartRepository.save(cart2);

        List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("Testing@naver.com", null, authorities)
        );


        mockMvc.perform(RestDocumentationRequestBuilders.get("/cart"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].productName").value("물품1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].productName").value("물품2"))
                .andDo(MockMvcRestDocumentation.document("cart/cart-list",
                        responseFields(
                                fieldWithPath("[]").description("카트 리스트"),
                                fieldWithPath("[].id").description("카트 번호"),
                                fieldWithPath("[].productId").description("상품 번호"),
                                fieldWithPath("[].productName").description("상품 이름"),
                                fieldWithPath("[].quantity").description("상품 수량")
                        )
                ))
                .andDo(MockMvcResultHandlers.print());


    }

    @Test
    @DisplayName("카트 상품 추가")
    @CustomMockMember(role = "ROLE_USER")
    void addCartProduct() throws Exception{
        //given
        Member member = Member.builder()
                .email("Testing@naver.com")
                .name("Test")
                .role("ROLE_USER")
                .password("ASDA515184424")
                .build();

        memberRepository.save(member);

        ProductCategory productCategory = ProductCategory.builder()
                .name("카테고리1")
                .build();

        productCategoryRepository.save(productCategory);

        Product product = Product.builder()
                .name("물품1")
                .price(1000)
                .quantity(10)
                .build();

        Product product2 = Product.builder()
                .name("물품2")
                .price(51)
                .quantity(100)
                .build();

        product.setCategory(productCategory);
        product2.setCategory(productCategory);

        productRepository.save(product);
        productRepository.save(product2);

        CartRequest cartRequest = CartRequest.builder()
                .productId(product.getId())
                .quantity(3)
                .build();

        CartRequest cartRequest2 = CartRequest.builder()
                .productId(product2.getId())
                .quantity(5)
                .build();


        List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("Testing@naver.com", null, authorities)
        );


        mockMvc.perform(RestDocumentationRequestBuilders.post("/cart")
                        .content(objectMapper.writeValueAsString(cartRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(MockMvcRestDocumentation.document("cart/cart-addProduct",
                        PayloadDocumentation.requestFields(
                                PayloadDocumentation.fieldWithPath("productId").description("상품 번호"),
                                PayloadDocumentation.fieldWithPath("quantity").description("상품 수량")
                        )
                ))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("카트 상품 수량 수정")
    @CustomMockMember(role = "ROLE_USER")
    void modifyCartProduct() throws Exception {
        //given
        Member member = Member.builder()
                .email("Testing@naver.com")
                .name("Test")
                .role("ROLE_USER")
                .password("ASDA515184424")
                .build();

        memberRepository.save(member);

        ProductCategory productCategory = ProductCategory.builder()
                .name("카테고리1")
                .build();

        productCategoryRepository.save(productCategory);

        Product product = Product.builder()
                .name("물품1")
                .price(1000)
                .quantity(10)
                .build();

        Product product2 = Product.builder()
                .name("물품2")
                .price(51)
                .quantity(100)
                .build();

        product.setCategory(productCategory);
        product2.setCategory(productCategory);

        productRepository.save(product);
        productRepository.save(product2);

        CartRequest cartRequest = CartRequest.builder()
                .productId(product.getId())
                .quantity(3)
                .build();

        Cart cart = Cart.createCart(member,product,cartRequest.getQuantity());

        cartRepository.save(cart);

        CartRequest cartRequest2 = CartRequest.builder()
                .productId(product.getId())
                .quantity(5)
                .build();


        List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("Testing@naver.com", null, authorities)
        );

        mockMvc.perform(RestDocumentationRequestBuilders.patch("/cart")
                    .content(objectMapper.writeValueAsString(cartRequest2))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(MockMvcRestDocumentation.document("cart/cart-modifyProduct",
                        PayloadDocumentation.requestFields(
                                PayloadDocumentation.fieldWithPath("productId").description("상품 번호"),
                                PayloadDocumentation.fieldWithPath("quantity").description("상품 수량")
                        )
                ))
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    @DisplayName("카트 상품 삭제")
    @CustomMockMember(role = "ROLE_USER")
    void removeCartProduct() throws Exception{
        //given
        Member member = Member.builder()
                .email("Testing@naver.com")
                .name("Test")
                .role("ROLE_USER")
                .password("ASDA515184424")
                .build();

        memberRepository.save(member);

        ProductCategory productCategory = ProductCategory.builder()
                .name("카테고리1")
                .build();

        productCategoryRepository.save(productCategory);

        Product product = Product.builder()
                .name("물품1")
                .price(1000)
                .quantity(10)
                .build();

        Product product2 = Product.builder()
                .name("물품2")
                .price(51)
                .quantity(100)
                .build();

        product.setCategory(productCategory);
        product2.setCategory(productCategory);

        productRepository.save(product);
        productRepository.save(product2);

        CartRequest cartRequest = CartRequest.builder()
                .productId(product.getId())
                .quantity(3)
                .build();

        Cart cart = Cart.createCart(member,product,cartRequest.getQuantity());

        cartRepository.save(cart);




        List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("Testing@naver.com", null, authorities)
        );

        mockMvc.perform(RestDocumentationRequestBuilders.delete("/cart")
                        .content(objectMapper.writeValueAsString(cartRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(MockMvcRestDocumentation.document("cart/cart-removeProduct",
                        PayloadDocumentation.requestFields(
                                PayloadDocumentation.fieldWithPath("productId").description("상품 번호"),
                                PayloadDocumentation.fieldWithPath("quantity").description("상품 수량")
                        )
                ))
                .andDo(MockMvcResultHandlers.print());

    }
}