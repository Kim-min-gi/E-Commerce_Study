package com.study.ecommerce.service;

import com.study.ecommerce.domain.ProductCategory;
import com.study.ecommerce.exception.AlreadyExistsCategory;
import com.study.ecommerce.exception.NotFoundCategory;
import com.study.ecommerce.repository.ProductCategoryRepository;
import com.study.ecommerce.request.CategoryRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductCategoryService {

    private final ProductCategoryRepository productCategoryRepository;


//    public List<ProductCategory> getCategory(){
//        return productCategoryRepository.findAll();
//    }

    public void addCategory(CategoryRequest categoryRequest){

        Optional<ProductCategory> productCategory = productCategoryRepository.findByName(categoryRequest.getName());


        if (productCategory.isPresent()){
            throw new AlreadyExistsCategory();
        }


        productCategoryRepository.save(ProductCategory.form(categoryRequest));

    }

    @Transactional
    public void modifyCategory(long id,CategoryRequest categoryRequest){
        ProductCategory productCategory = productCategoryRepository.findById(id)
                .orElseThrow(NotFoundCategory::new);

        productCategory.modifyCategoryName(categoryRequest);

    }

    public void removeCategory(long id){
        productCategoryRepository.findById(id).orElseThrow(NotFoundCategory::new);

        productCategoryRepository.deleteById(id);
    }




}
