package com.study.ecommerce.request;

import com.study.ecommerce.domain.Member;
import com.study.ecommerce.exception.AdminCodeNotMatch;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
@Builder
public class MemberSignUp {


    @Email(message = "이메일 형식을 확인해주세요")
    @NotBlank(message = "이메일은 필수 입력입니다.")
    private String email;

    @NotBlank(message = "이름은 필수 입력입니다.")
    private String name;

    @NotBlank(message = "비밀번호는 필수 입력입니다.")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,15}$"
    , message = "비밀번호는 영어 대소문자, 숫자, 특수문자(~!@#$)를 포함한 8~11 자리로 입력해야 합니다.")
    private String password;

    private String code;



}
