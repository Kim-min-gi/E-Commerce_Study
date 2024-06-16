package com.study.ecommerce.domain;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int bill;

    @Column(nullable = false)
    private int amount;



    @Builder
    public Product(String name, int bill, int amount) {
        this.name = name;
        this.bill = bill;
        this.amount = amount;
    }



}
