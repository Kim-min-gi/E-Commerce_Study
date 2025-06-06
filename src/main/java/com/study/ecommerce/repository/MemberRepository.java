package com.study.ecommerce.repository;

import com.study.ecommerce.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long> {

    Optional<Member> findByEmail(String email);
    Page<Member> findAll(Pageable pageable);

    boolean existsByEmail(String email);
}
