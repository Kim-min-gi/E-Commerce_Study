package com.study.ecommerce.service;

import com.study.ecommerce.domain.Product;
import com.study.ecommerce.exception.AlreadyExistsProduct;
import com.study.ecommerce.repository.ProductRepository;
import com.study.ecommerce.request.ProductRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public void addProduct(ProductRequest productRequest){

        Optional<Product> findProduct = productRepository.findByName(productRequest.getName());

        if (findProduct.isPresent()){
            throw new AlreadyExistsProduct();
        }

        productRepository.save(Product.form(productRequest));

    }



}
