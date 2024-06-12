package com.study.ecommerce.response;

import com.study.ecommerce.domain.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
public class AdminResponse {

    private final Long id;
    private final String email;
    private final String name;

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


}
