//package com.study.ecommerce.domain;
//
//
//import jakarta.persistence.*;
//import jakarta.validation.constraints.NotBlank;
//import lombok.Builder;
//import lombok.NoArgsConstructor;
//import lombok.RequiredArgsConstructor;
//
//import java.time.LocalDateTime;
//
//@Entity
//@Builder
//@NoArgsConstructor
//public class Product {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "product_id")
//    private long id;
//
//    @Column(nullable = false)
//    private String name;
//
//    @Column(nullable = false)
//    private long bill;
//
//    @Column(nullable = false)
//    private String intro;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "product_id")
//    private Category category;
//
//    private LocalDateTime rgsDt; //등록일시
//    private LocalDateTime mdfDt; //수정일시
//
//    public Product(long id, String name, long bill, String intro, Category category, LocalDateTime rgsDt, LocalDateTime mdfDt) {
//        this.id = id;
//        this.name = name;
//        this.bill = bill;
//        this.intro = intro;
//        this.category = category;
//        this.rgsDt = rgsDt;
//        this.mdfDt = mdfDt;
//    }
//
//
//}
