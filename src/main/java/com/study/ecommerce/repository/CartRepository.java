package com.study.ecommerce.repository;

import com.study.ecommerce.domain.Cart;
import com.study.ecommerce.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart,Long> {

    List<Cart> findAllByMember(Member member);

    void deleteAllByMember(Member member);
}
