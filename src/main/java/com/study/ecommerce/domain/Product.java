package com.study.ecommerce.domain;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Builder
@RequiredArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "상품 이름은 필수입니다.")
    private String name;

    @NotBlank(message = "상품 가격은 필수입니다.")
    private long bill;

    @NotBlank(message = "상품 설명은 필수입니다.")
    private String intro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Category category;

    private LocalDateTime rgsDt; //등록일시
    private LocalDateTime mdfDt; //수정일시

}
