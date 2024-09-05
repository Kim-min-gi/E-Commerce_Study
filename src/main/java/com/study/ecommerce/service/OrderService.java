package com.study.ecommerce.service;

import com.study.ecommerce.domain.Member;
import com.study.ecommerce.domain.Order;
import com.study.ecommerce.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;


    public Page<Order> findAllOrders(Member member, Pageable pageable){
        return orderRepository.findByMember(member,pageable);
    }



}
