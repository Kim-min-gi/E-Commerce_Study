package com.study.ecommerce.response;

import com.study.ecommerce.domain.Product;
import com.study.ecommerce.domain.ProductCategory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Getter
public class ProductResponse {

    private long id;
    private String name;
    private int price;
    private int amount;
    private ProductCategory productCategory;


}
