package com.study.ecommerce.domain;

import com.study.ecommerce.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cart extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Product cartProduct;

    @Column(nullable = false)
    private Integer quantity;


    @Builder
    public Cart(Member member, Product cartProduct, Integer quantity) {
        this.member = member;
        this.cartProduct = cartProduct;
        this.quantity = quantity;
    }

    public void addQuantity(Integer quantity){
        this.quantity += quantity;
    }

    public void setQuantity(Integer quantity){
        this.quantity = quantity;
    }

    public static Cart createCart(Member member, Product product, Integer quantity){
        return Cart.builder()
                .member(member)
                .cartProduct(product)
                .quantity(quantity)
                .build();
    }

}
