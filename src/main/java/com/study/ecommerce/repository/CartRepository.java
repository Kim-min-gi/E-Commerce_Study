package com.study.ecommerce.repository;

import com.study.ecommerce.domain.Cart;
import com.study.ecommerce.domain.Member;
import com.study.ecommerce.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart,Long> {

    List<Cart> findAllByMember(Member member);

    Optional<Cart> findByMemberAndCartProduct(Member member, Product product);

    void deleteAllByMember(Member member);
}
