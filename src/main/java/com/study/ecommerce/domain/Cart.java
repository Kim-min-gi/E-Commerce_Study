package com.study.ecommerce.domain;

import com.study.ecommerce.common.BaseTimeEntity;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Cart extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int amount;

    @Column(nullable = false)
    private int totalPrice;

    @OneToOne
    @JoinColumn(name = "cart")
    private Member cartMember;

    @OneToMany(mappedBy = "cart",fetch = FetchType.LAZY)
    private List<Product> product;






}
