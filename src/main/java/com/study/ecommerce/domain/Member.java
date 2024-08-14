package com.study.ecommerce.domain;

import com.study.ecommerce.common.BaseTimeEntity;
import com.study.ecommerce.request.MemberSignUp;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,unique = true,length = 50)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role;

    @Embedded
    private Address address;



    @Builder
    public Member(String email, String name, String password,String role) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.role = role;
    }


    @Builder
    public static Member from(MemberSignUp memberSignUp, String encodePassword, String role){
        return Member.builder()
                .email(memberSignUp.getEmail())
                .password(encodePassword)
                .name(memberSignUp.getName())
                .role(role)
                .build();
    }




}
