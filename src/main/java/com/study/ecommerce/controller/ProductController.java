package com.study.ecommerce.controller;

import com.study.ecommerce.domain.Product;
import com.study.ecommerce.request.ProductRequest;
import com.study.ecommerce.response.ProductResponse;
import com.study.ecommerce.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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

    @GetMapping("/admin/products")
    public ResponseEntity<List<ProductResponse>> getProducts(@PageableDefault Pageable pageable){

        List<ProductResponse> products =  productService.getProducts(pageable);

        return ResponseEntity.ok(products);
    }

    @GetMapping("/admin/product/{id}")
    public ResponseEntity<ProductResponse> getAdminProduct(@PathVariable long id){

        ProductResponse product = productService.getProduct(id);

        return ResponseEntity.ok(product);
    }

    @PatchMapping("/admin/product/{id}")
    public ResponseEntity<Void> modifyProduct(@PathVariable long id,@Valid @RequestBody ProductRequest productRequest){

        productService.modifyProduct(id,productRequest);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/admin/product/{id}")
    public ResponseEntity<Void> removeProduct(@PathVariable long id){

        productService.removeProduct(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    //user

    @GetMapping("/product/{id}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable long id){

        ProductResponse product = productService.getProduct(id);

        return ResponseEntity.ok(product);
    }


}
