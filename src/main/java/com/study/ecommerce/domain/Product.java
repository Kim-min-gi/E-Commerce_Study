package com.study.ecommerce.domain;


import com.study.ecommerce.common.BaseTimeEntity;
import com.study.ecommerce.request.ProductRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private int price;

    @NotNull
    private int amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private ProductCategory productCategory;


    @Builder
    public Product(String name, int price, int amount) {
        this.name = name;
        this.price = price;
        this.amount = amount;
    }

    public static Product form(ProductRequest productRequest){
        return  Product.builder()
                .name(productRequest.getName())
                .price(productRequest.getPrice())
                .amount(productRequest.getAmount())
                .build();
    }

    public void setCategory(ProductCategory productCategory){
        productCategory.getProducts().add(this);
        this.productCategory = productCategory;
    }


    public void modifyProduct(ProductRequest productRequest,ProductCategory productCategory){
        if (productRequest.getName() != null){
            this.name = productRequest.getName();
        }

        if (productRequest.getPrice() != null){
            this.price = productRequest.getPrice();
        }

        if (productRequest.getAmount() != null){
            this.amount = productRequest.getAmount();
        }

        if (productCategory != null){
            this.productCategory.getProducts().remove(this);
            setCategory(productCategory);
        }

    }



}
