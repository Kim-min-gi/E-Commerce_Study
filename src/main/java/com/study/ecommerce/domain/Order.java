package com.study.ecommerce.domain;

import com.study.ecommerce.common.BaseTimeEntity;
import com.study.ecommerce.domain.type.Payment;
import com.study.ecommerce.domain.type.OrderStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;

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
    private long totalPrice;

    @Embedded
    private Address address;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderProduct> orderProducts = new ArrayList<>();


    @Builder
    public Order(Member member, Payment payment, long totalPrice, Address address, OrderStatus orderStatus) {
        this.member = member;
        this.payment = payment;
        this.totalPrice = totalPrice;
        this.address = address;
        this.orderStatus = orderStatus;
    }

    public void setOrderStatusModify(OrderStatus orderStatus){
        this.orderStatus = orderStatus;
    }

    public void addOrderProduct(OrderProduct orderProduct){
        orderProduct.setOrder(this);
        this.orderProducts.add(orderProduct);
    }





}
