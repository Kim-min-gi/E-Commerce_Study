package com.study.ecommerce.controller;

import com.study.ecommerce.request.CategoryRequest;
import com.study.ecommerce.service.ProductCategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProductCategoryController {

    private final ProductCategoryService categoryService;


    @PostMapping("/admin/category")
    public ResponseEntity<Void> addCategory(@Valid @RequestBody CategoryRequest categoryRequest){

        categoryService.addCategory(categoryRequest);

        return ResponseEntity.status(200).build();
    }





}
