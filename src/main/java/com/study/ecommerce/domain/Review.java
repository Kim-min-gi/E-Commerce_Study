package com.study.ecommerce.domain;

import com.study.ecommerce.common.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseTimeEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn
//    private Member member;

    @NotNull
    private Long memberId;

    @NotBlank
    private String memberName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private OrderProduct orderProduct;

    @Column(nullable = false)
    private int rating;

    private String content;




}
