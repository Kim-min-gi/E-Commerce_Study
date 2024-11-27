package com.study.ecommerce.repository;

import com.study.ecommerce.domain.Member;
import com.study.ecommerce.domain.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;


public interface OrderRepository extends JpaRepository<Order,Long> {

    Page<Order> findByMember(Member member,Pageable pageable);

    Page<Order> findByMemberAndCreatedDateBetween(Member member, Pageable pageable, LocalDateTime startDate, LocalDateTime endDate);

}
