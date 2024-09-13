package com.study.ecommerce.repository;

import com.study.ecommerce.domain.Member;
import com.study.ecommerce.domain.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;


public interface OrderRepository extends JpaRepository<Order,Long> {

    Page<Order> findByMember(Member member,Pageable pageable);

    Page<Order> findByMember(Member member, Pageable pageable, LocalDate startDate, LocalDate endDate);

}
