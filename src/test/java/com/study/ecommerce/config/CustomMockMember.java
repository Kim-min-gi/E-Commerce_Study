package com.study.ecommerce.config;


import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = CustomMockSecurityContext.class)
public @interface CustomMockMember {

    String email() default "Testing@naver.com";

    String password() default "";

    String name() default "Test";

    String role() default "ROLE_ADMIN";



}
