package com.study.ecommerce.response;

import com.study.ecommerce.domain.Member;
import com.study.ecommerce.domain.Product;
import lombok.Builder;
import lombok.Getter;

@Getter
public class AdminResponse {

    private Long id;
    private String email;
    private String name;

    public AdminResponse(Member member) {
        this.id = member.getId();
        this.email = member.getEmail();
        this.name = member.getName();
    }

    @Builder
    public AdminResponse(Long id, String email, String name) {
        this.id = id;
        this.email = email;
        this.name = name;
    }
    public static AdminResponse from(Member member) {
        return AdminResponse.builder()
                .id(member.getId())
                .email(member.getEmail())
                .name(member.getName())
                .build();
    }




}
