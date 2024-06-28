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
@RequiredArgsConstructor
@Builder
public class ProductCategory extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    private String name;

    @OneToMany(mappedBy = "productCategory",fetch = FetchType.LAZY)
    private List<Product> product;


    public void modifyProductCategoryName(String name){
        this.name = name;
    }


    public static ProductCategory form(CategoryRequest categoryRequest){
        return ProductCategory.builder()
                .name(categoryRequest.getName())
                .build();
    }

    public void addProduct(Product product){
        this.product.add(product);
        product.addCategory(this);
    }



}
