package com.study.ecommerce.controller;

import com.study.ecommerce.request.ProductRequest;
import com.study.ecommerce.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;


    @PostMapping("/admin/product")
    public ResponseEntity<Void> addProduct(@Valid @RequestBody ProductRequest productRequest){

        productService.addProduct(productRequest);

        return ResponseEntity.status(200).build();
    }

    @GetMapping("/admin/product")
    public ResponseEntity<Void> getProducts(){   //페이징 작업

        productService.getProducts();

        return ResponseEntity.status(200).build();
    }

    @GetMapping("/admin/product")
    public ResponseEntity<Void> getProduct(@Valid @RequestBody ProductRequest productRequest){

        productService.addProduct(productRequest);

        return ResponseEntity.status(200).build();
    }


}
