package com.study.ecommerce.repository;

import com.study.ecommerce.domain.RefreshToken;
import org.springframework.data.repository.CrudRepository;


public interface RefreshTokenRepository extends CrudRepository<RefreshToken,String> {

}
