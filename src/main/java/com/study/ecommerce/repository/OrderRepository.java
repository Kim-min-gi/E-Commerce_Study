package com.study.ecommerce.repository;

import com.study.ecommerce.domain.Member;
import com.study.ecommerce.domain.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OrderRepository extends JpaRepository<Order,Long> {

    Page<Order> findByMember(Member member,Pageable pageable);

}
