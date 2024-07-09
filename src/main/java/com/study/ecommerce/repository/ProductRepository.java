package com.study.ecommerce.repository;

import com.study.ecommerce.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,Long> {

    Optional<Product> findByName(String name);

    @Query("SELECT p FROM Product p JOIN FETCH p.productCategory")
    Page<Product> findAllWithCategory(Pageable pageable);

}
