package com.study.ecommerce.domain;

import com.study.ecommerce.request.MemberSignUp;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;


    @Column(unique = true,length = 50)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role;

    @OneToMany(mappedBy ="member")
    private List<Order> orders;

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
