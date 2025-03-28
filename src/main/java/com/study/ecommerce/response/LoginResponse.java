package com.study.ecommerce.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Builder;
import lombok.Getter;

@Builder
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class LoginResponse {

    private AuthenticatedResponseDto authenticatedResponseDto;
    private MemberInfoResponseDto memberInfoResponseDto;

    @Getter
    public static class AuthenticatedResponseDto{
        private final String grantType;
        private final String accessToken;
        private final String refreshToken;

        @Builder
        public AuthenticatedResponseDto(String grantType, String accessToken, String refreshToken) {
            this.grantType = grantType;
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
        }
    }

    @Getter
    public static class MemberInfoResponseDto {
        private final String email;
        private final String role;

        @Builder
        public MemberInfoResponseDto(String email, String role) {
            this.email = email;
            this.role = role;
        }
    }



}
