package com.study.ecommerce.repository;

import com.study.ecommerce.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member,Long> {

    
}
