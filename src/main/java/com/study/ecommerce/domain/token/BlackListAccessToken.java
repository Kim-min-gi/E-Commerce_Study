package com.study.ecommerce.domain.token;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@NoArgsConstructor
@RedisHash(value = "accessToken", timeToLive = 600L)
public class BlackListAccessToken {

    @Id
    private String token;

    @Builder
    public BlackListAccessToken(String token) {
        this.token = token;
    }
}
