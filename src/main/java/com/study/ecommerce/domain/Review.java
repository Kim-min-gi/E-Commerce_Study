package com.study.ecommerce.domain;

import com.study.ecommerce.common.BaseTimeEntity;
import jakarta.persistence.*;

@Entity
public class Review extends BaseTimeEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int rating;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Member reviewMember;



}
