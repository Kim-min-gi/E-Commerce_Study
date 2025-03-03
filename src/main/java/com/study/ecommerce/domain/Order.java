package com.study.ecommerce.domain;

import com.study.ecommerce.common.BaseTimeEntity;
import com.study.ecommerce.domain.type.Payment;
import com.study.ecommerce.domain.type.OrderStatus;
import com.study.ecommerce.response.OrderResponse;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Order extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Payment payment;

    @Column(nullable = false)
    private int totalPrice;

    @Embedded
    private Address address;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;


    @Builder
    public Order(Member member, Payment payment, int totalPrice, Address address, OrderStatus orderStatus) {
        this.member = member;
        this.payment = payment;
        this.totalPrice = totalPrice;
        this.address = address;
        this.orderStatus = orderStatus;
    }

    public void setOrderModify(OrderStatus orderStatus){
        this.orderStatus = orderStatus;
    }





}
