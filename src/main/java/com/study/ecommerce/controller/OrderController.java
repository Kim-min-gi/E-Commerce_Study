package com.study.ecommerce.controller;

import com.study.ecommerce.domain.Member;
import com.study.ecommerce.domain.Order;
import com.study.ecommerce.service.OrderService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/orders")
    public Page<Order> findAllOrders(Member member, Pageable pageable){
        return orderService.findAllOrders(member,pageable);
    }


}
