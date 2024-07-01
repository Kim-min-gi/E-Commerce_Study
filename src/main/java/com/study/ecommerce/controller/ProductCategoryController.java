package com.study.ecommerce.controller;

import com.study.ecommerce.request.CategoryRequest;
import com.study.ecommerce.service.ProductCategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ProductCategoryController {

    private final ProductCategoryService categoryService;


    @PostMapping("/admin/category")
    public ResponseEntity<Void> addCategory(@Valid @RequestBody CategoryRequest categoryRequest){

        categoryService.addCategory(categoryRequest);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/admin/category/{id}")
    public ResponseEntity<Void> modifyCategory(
            @PathVariable long id
            ,@Valid @RequestBody CategoryRequest categoryRequest){

        categoryService.modifyCategory(categoryRequest);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/admin/category/{id}")
    public ResponseEntity<Void> removeCategory(@PathVariable long id){

        categoryService.removeCategory(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }




}
