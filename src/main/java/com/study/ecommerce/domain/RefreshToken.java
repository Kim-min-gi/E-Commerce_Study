package com.study.ecommerce.domain;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@NoArgsConstructor
@RedisHash(value = "refreshToken", timeToLive = 86400000L)
public class RefreshToken {


    @Id
    private String email;

    private String token;


    @Builder
    public RefreshToken(String email, String token) {
        this.email = email;
        this.token = token;
    }
}
