package com.study.ecommerce.domain;


import com.study.ecommerce.common.BaseTimeEntity;
import com.study.ecommerce.request.ProductRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private ProductCategory productCategory;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<CartProduct> cartItem;


    @Builder
    public Product(String name, int price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public static Product form(ProductRequest productRequest){
        return  Product.builder()
                .name(productRequest.getName())
                .price(productRequest.getPrice())
                .quantity(productRequest.getQuantity())
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

        if (productRequest.getQuantity() != null){
            this.quantity = productRequest.getQuantity();
        }

        if (productCategory != null){
            this.productCategory.getProducts().remove(this);
            setCategory(productCategory);
        }

    }



}
