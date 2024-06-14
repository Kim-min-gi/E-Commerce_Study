package com.study.ecommerce.config;

import com.study.ecommerce.domain.Member;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.List;

@Getter
public class UserPrincipal extends User {

    private final long memberId;


    // ROLE_ADMIN 으로 해야 역할
    // ADMIN 같은 경우는 권한
    public UserPrincipal(Member member){
        super(member.getEmail(),member.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_USER")
                                                             // ,new SimpleGrantedAuthority("WRITE")
                ));
        this.memberId = member.getId();
    }

}
