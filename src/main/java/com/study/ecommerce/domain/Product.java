package com.study.ecommerce.domain;


import com.study.ecommerce.common.BaseTimeEntity;
import com.study.ecommerce.request.ProductRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

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
    public Product(String name, int price, int amount,ProductCategory productCategory) {
        this.name = name;
        this.price = price;
        this.amount = amount;
        this.productCategory =  productCategory;
    }

    public static Product form(ProductRequest productRequest){
        return  Product.builder()
                .name(productRequest.getName())
                .price(productRequest.getPrice())
                .amount(productRequest.getAmount())
                .productCategory(productRequest.getProductCategory())
                .build();
    }

    public void addCategory(ProductCategory productCategory){
        this.productCategory = productCategory;
        productCategory.addProduct(this);
    }

}
