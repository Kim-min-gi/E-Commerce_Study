package com.study.ecommerce.request;

import com.study.ecommerce.domain.Member;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberSignUp {

    private long id;
    private String email;
    private String name;
    private String password;


    public Member toEntity(){
        return Member.builder()
                .email(this.getEmail())
                .name(this.getName())
                .password(this.getPassword())
                .build();
    }

}
