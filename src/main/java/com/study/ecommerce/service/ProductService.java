package com.study.ecommerce.service;

import com.study.ecommerce.domain.Product;
import com.study.ecommerce.domain.ProductCategory;
import com.study.ecommerce.exception.AlreadyExistsProduct;
import com.study.ecommerce.exception.NotFoundCategory;
import com.study.ecommerce.exception.NotFoundProduct;
import com.study.ecommerce.repository.ProductCategoryRepository;
import com.study.ecommerce.repository.ProductRepository;
import com.study.ecommerce.request.ProductRequest;
import com.study.ecommerce.response.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;

    @Transactional
    public void addProduct(ProductRequest productRequest){

        Optional<Product> findProduct = productRepository.findByName(productRequest.getName());

        //중복체크
        if (findProduct.isPresent()){
            throw new AlreadyExistsProduct();
        }

        ProductCategory findProductCategory = productCategoryRepository.findByName(productRequest.getCategoryName())
                .orElseThrow(NotFoundCategory::new);

        var product = Product.form(productRequest);
        product.setCategory(findProductCategory);

        productRepository.save(product);

    }

    public List<ProductResponse> getProducts(Pageable pageable) {

        return productRepository.findAllWithCategory(pageable).stream().map(ProductResponse::from).
                toList();
    }

    @Transactional(readOnly = true)
    public ProductResponse getProduct(long id){
        Product product  = productRepository.findById(id).orElseThrow(NotFoundProduct::new);

        return ProductResponse.from(product);
    }


    @Transactional
    public void modifyProduct(long id, ProductRequest productRequest){
        Product product = productRepository.findById(id)
                .orElseThrow(NotFoundProduct::new);

        ProductCategory productCategory = null;

        if (!productRequest.getCategoryName().isEmpty()){
            productCategory = productCategoryRepository.findByName(productRequest.getCategoryName())
                    .orElseThrow(NotFoundCategory::new);
        }


        product.modifyProduct(productRequest,productCategory);

    }

    public void removeProduct(long id){
        productRepository.findById(id).orElseThrow(NotFoundProduct::new);

        productRepository.deleteById(id);
    }


    ///user용


    //카테고리별 상품 리스트


    //베스트 상품 리스트?

    //상품 상세 조회







}
