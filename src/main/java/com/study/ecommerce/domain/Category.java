//package com.study.ecommerce.domain;
//
//import jakarta.persistence.*;
//import jakarta.validation.constraints.NotBlank;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Entity
//public class Category {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private long id;
//
//    @Column(nullable = false)
//    private String categoryName;
//
//    @OneToMany(mappedBy = "category")
//    private List<Product> products = new ArrayList<>();
//
//
//}
