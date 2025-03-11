package com.study.ecommerce.repository;

import com.study.ecommerce.domain.Cart;
import com.study.ecommerce.domain.Member;
import com.study.ecommerce.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart,Long> {
    @Query("SELECT c FROM Cart c JOIN FETCH c.member JOIN FETCH c.cartProduct WHERE c.member = :member")
    List<Cart> findAllByMember(@Param("member") Member member);

    @Query("SELECT c FROM Cart c JOIN FETCH c.member JOIN FETCH c.cartProduct WHERE c.member = :member and c.cartProduct = :cartProduct")
    Optional<Cart> findByMemberAndCartProduct(@Param("member") Member member, @Param("cartProduct") Product product);

    void deleteAllByMember(Member member);
}
