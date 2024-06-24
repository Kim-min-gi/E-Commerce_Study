package com.study.ecommerce.request;

import com.study.ecommerce.domain.Member;
import com.study.ecommerce.exception.AdminCodeNotMatch;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
@Builder
public class MemberSignUp {

    private long id;
    private String email;
    private String name;
    private String password;
    private String code;


    public Member toEntity(){
        return Member.builder()
                .email(this.getEmail())
                .name(this.getName())
                .password(this.getPassword())
                .build();
    }




}
