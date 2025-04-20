package com.study.ecommerce.controller;

import com.study.ecommerce.request.ProductRequest;
import com.study.ecommerce.response.ProductResponse;
import com.study.ecommerce.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;


    @PostMapping("/admin/product")
    public ResponseEntity<Void> addProduct(@Valid @RequestBody ProductRequest productRequest){

        productService.addProduct(productRequest);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/product/products")
    public ResponseEntity<List<ProductResponse>> getProducts(@PageableDefault Pageable pageable){

        List<ProductResponse> products =  productService.getProducts(pageable);

        return ResponseEntity.ok(products);
    }

    @GetMapping("/admin/product/{productId}")
    public ResponseEntity<ProductResponse> getAdminProduct(@PathVariable Long productId){

        ProductResponse product = productService.getProduct(productId);

        return ResponseEntity.ok(product);
    }

    @PatchMapping("/admin/product/{productId}")
    public ResponseEntity<Void> modifyProduct(@PathVariable Long productId,@Valid @RequestBody ProductRequest productRequest){

        productService.modifyProduct(productId,productRequest);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/admin/product/{productId}")
    public ResponseEntity<Void> removeProduct(@PathVariable Long productId){

        productService.removeProduct(productId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    //user

    @GetMapping("/product/{productId}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable Long productId){

        ProductResponse product = productService.getProduct(productId);

        return ResponseEntity.ok(product);
    }

    @GetMapping("/product/list/{categoryId}")
    public ResponseEntity<List<ProductResponse>> getCategoryProduct(@PathVariable Long categoryId){

        List<ProductResponse> categoryProduct = productService.getCategoryProduct(categoryId);

        return ResponseEntity.ok(categoryProduct);
    }


}
