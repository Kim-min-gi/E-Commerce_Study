package com.study.ecommerce.domain;


import com.study.ecommerce.common.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Product extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    private String name;

    @NotNull
    private int bill;

    @NotNull
    private int amount;


    @Builder
    public Product(String name, int bill, int amount) {
        this.name = name;
        this.bill = bill;
        this.amount = amount;
    }



}
