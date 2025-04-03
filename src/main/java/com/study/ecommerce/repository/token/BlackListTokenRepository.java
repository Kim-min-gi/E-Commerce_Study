package com.study.ecommerce.repository.token;

import com.study.ecommerce.domain.token.BlackListAccessToken;
import org.springframework.data.repository.CrudRepository;

public interface BlackListTokenRepository extends CrudRepository<BlackListAccessToken,String> {

}
