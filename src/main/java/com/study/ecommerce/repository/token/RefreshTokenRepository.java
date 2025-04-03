package com.study.ecommerce.repository.token;

import com.study.ecommerce.domain.token.RefreshToken;
import org.springframework.data.repository.CrudRepository;


public interface RefreshTokenRepository extends CrudRepository<RefreshToken,String> {

}
