package com.study.ecommerce.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.ecommerce.config.handler.Http401Handler;
import com.study.ecommerce.config.handler.Http403Handler;
import com.study.ecommerce.config.handler.LoginFailHandler;
import com.study.ecommerce.domain.Member;
import com.study.ecommerce.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final ObjectMapper objectMapper;


    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){ //spring security 아예 안타게
        return new WebSecurityCustomizer() {
            @Override
            public void customize(WebSecurity web) {
                web.ignoring().requestMatchers("/favicon.ico","/error")
                        .requestMatchers(PathRequest.toH2Console());
            }
        };
    }


    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/auth/login","auth/signup").permitAll()
                        .requestMatchers("/member").hasAnyRole("USER","ADMIN")
                        //.requestMatchers("/admin").hasRole("ADMIN")
                        .requestMatchers("/admin/**")
                            .access(new WebExpressionAuthorizationManager("hasRole('ADMIN') AND hasAuthority('WRITE')"))
                        .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        .loginPage("/auth/login")
                        .loginProcessingUrl("/auth/login")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/")
                        .failureHandler(new LoginFailHandler(objectMapper))
                )
                .rememberMe(rm -> rm.rememberMeParameter("remember")
                        .alwaysRemember(false)
                        .tokenValiditySeconds(2592000)
                )
                .exceptionHandling(e -> e.accessDeniedHandler(new Http403Handler(objectMapper))
                                    .authenticationEntryPoint(new Http401Handler(objectMapper))
                )
                .build();
    }



    @Bean
    public UserDetailsService userDetailsService(MemberRepository memberRepository){
            return email -> {
                Member member = memberRepository.findByEmail(email)
                        .orElseThrow(() -> new UsernameNotFoundException("아이디와 비밀번호를 확인해주세요."));

                return new UserPrincipal(member);

            };
    }





}
