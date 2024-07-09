package com.study.ecommerce.domain;

import com.study.ecommerce.common.BaseTimeEntity;
import com.study.ecommerce.request.CategoryRequest;
import com.study.ecommerce.request.ProductRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductCategory extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @OneToMany(mappedBy = "productCategory")
    private List<Product> products = new ArrayList<>();


    public void modifyProductCategoryName(String name){
        this.name = name;
    }

    @Builder
    public ProductCategory(String name, List<Product> product) {
        this.name = name;
        this.products = product == null ? new ArrayList<>() : product;
    }

    public static ProductCategory form(CategoryRequest categoryRequest){
        return ProductCategory.builder()
                .name(categoryRequest.getName())
                .build();
    }

    public void addProduct(Product product){
        this.products.add(product);
    }

    public void modifyCategory(CategoryRequest categoryRequest){
        this.name = categoryRequest.getName();
    }



}
