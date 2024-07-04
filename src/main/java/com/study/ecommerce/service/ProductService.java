package com.study.ecommerce.service;

import com.study.ecommerce.domain.Product;
import com.study.ecommerce.domain.ProductCategory;
import com.study.ecommerce.exception.AlreadyExistsProduct;
import com.study.ecommerce.exception.NotFoundCategory;
import com.study.ecommerce.exception.NotFoundProduct;
import com.study.ecommerce.repository.ProductCategoryRepository;
import com.study.ecommerce.repository.ProductRepository;
import com.study.ecommerce.request.ProductRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;

    public void addProduct(ProductRequest productRequest){

        Optional<Product> findProduct = productRepository.findByName(productRequest.getName());
        Optional<ProductCategory> findProductCategory = productCategoryRepository.findById(productRequest.getProductCategory().getId());

        var product = Product.form(productRequest);
        product.addCategory(findProductCategory.get());


        if (findProduct.isPresent()){
            throw new AlreadyExistsProduct();
        }

        productRepository.save(product);

    }


    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProduct(long id){
        return productRepository.findById(id);
    }


    public void modifyProduct(long id, ProductRequest productRequest){
        Product product = productRepository.findById(id)
                .orElseThrow(NotFoundProduct::new);

        product.modifyProduct(productRequest);

        productRepository.save(product);

    }

    public void removeProduct(long id){
        productRepository.findById(id).orElseThrow(NotFoundProduct::new);

        productRepository.deleteById(id);
    }







}
