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

    @NotNull
    @Column(unique = true,length = 50)
    private String email;

    @NotNull
    private String name;

    @NotNull
    private String password;

    @NotNull
    private String role;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "member",fetch = FetchType.LAZY)
    private List<Order> orders;

    @OneToMany(mappedBy = "reviewMember",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<Review> reviews;

    @OneToOne(mappedBy = "member",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Cart cart;

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
