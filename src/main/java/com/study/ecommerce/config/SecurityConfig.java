package com.study.ecommerce.config;

import com.study.ecommerce.domain.Member;
import com.study.ecommerce.repository.MemberRepository;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.nio.file.attribute.UserPrincipal;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        return http
                .csrf(Customizer.withDefaults())
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/auth/login","auth/signup").permitAll()
                        .requestMatchers("/member").hasAnyRole("USER","ADMIN")
                        .requestMatchers("/admin").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        .loginPage("/auth/login")
                        .loginProcessingUrl("/auth/login")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/")
                )
                .build();
    }



//    @Bean
//    public UserDetailsService userDetailsService(MemberRepository memberRepository){
//            return email -> {
//                Member member = memberRepository.findByEmail(email)
//                        .orElseThrow(() -> new UsernameNotFoundException("아이디와 비밀번호를 확인해주세요."));
//
//                return new UserPrincipal(member);
//
//            };
//    }


    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
            return new WebSecurityCustomizer() {
                @Override
                public void customize(WebSecurity web) {
                        web.ignoring().requestMatchers("/favicon.ico","/error")
                                .requestMatchers(PathRequest.toH2Console());
                }
            };
    }









}
