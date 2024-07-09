package com.study.ecommerce.domain;

import com.study.ecommerce.common.BaseTimeEntity;
import com.study.ecommerce.request.CategoryRequest;
import jakarta.persistence.*;
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

    @Column(nullable = false,unique = true)
    private String name;

    @OneToMany(mappedBy = "productCategory" , cascade = CascadeType.ALL)
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
        product.setCategory(this);
        this.products.add(product);
    }

    public void modifyCategoryName(CategoryRequest categoryRequest){
        this.name = categoryRequest.getName();
    }

}
