package com.study.ecommerce.response;


import com.study.ecommerce.domain.ProductCategory;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductCategoryResponse {

    private Long id;

    private String name;

    private Long productCount;




    public void setProductCount(){

    }

    public static ProductCategoryResponse from(ProductCategory productCategory){
        return ProductCategoryResponse.builder()
                .id(productCategory.getId())
                .name(productCategory.getName())
                .productCount(null)
                .build();
    }

}
