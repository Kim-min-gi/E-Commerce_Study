package com.study.ecommerce.repository;

import com.study.ecommerce.domain.Member;
import com.study.ecommerce.domain.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface OrderRepository extends JpaRepository<Order,Long> {

    Page<Order> findByMember(Member member,Pageable pageable);

    @Query("SELECT DISTINCT o FROM Order o " +
            "JOIN FETCH o.orderProducts op " +
            "JOIN FETCH op.product p " +
            "JOIN FETCH o.member m " +
            "WHERE o.id = :orderId")
    Optional<Order> findByOrderId(@Param("orderId") Long orderId);

    Page<Order> findByMemberAndCreatedDateBetween(Member member, Pageable pageable, LocalDateTime startDate, LocalDateTime endDate);

}
