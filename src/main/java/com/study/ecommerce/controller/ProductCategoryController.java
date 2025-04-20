package com.study.ecommerce.controller;

import com.study.ecommerce.request.CategoryRequest;
import com.study.ecommerce.response.ProductCategoryResponse;
import com.study.ecommerce.service.ProductCategoryService;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductCategoryController {

    private final ProductCategoryService categoryService;


    @GetMapping("/categories")
    public ResponseEntity<List<ProductCategoryResponse>> getCategoryList(){

        List<ProductCategoryResponse> categoryList = categoryService.getCategoryList();

        return ResponseEntity.ok(categoryList);
    }


    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ProductCategoryResponse> getCategory(@PathVariable Long categoryId){

        ProductCategoryResponse category = categoryService.getCategory(categoryId);

        return ResponseEntity.ok(category);
    }



    @PostMapping("/admin/category")
    public ResponseEntity<Void> addCategory(@Valid @RequestBody CategoryRequest categoryRequest){

        categoryService.addCategory(categoryRequest);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/admin/category/{categoryId}")
    public ResponseEntity<Void> modifyCategory(
            @PathVariable Long categoryId
            ,@Valid @RequestBody CategoryRequest categoryRequest){

        categoryService.modifyCategory(categoryId,categoryRequest);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/admin/category/{categoryId}")
    public ResponseEntity<Void> removeCategory(@PathVariable Long categoryId){

        categoryService.removeCategory(categoryId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }




}
