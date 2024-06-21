package com.study.ecommerce.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.ecommerce.config.filter.LoginFilter;
import com.study.ecommerce.config.handler.Http401Handler;
import com.study.ecommerce.config.handler.Http403Handler;
import com.study.ecommerce.config.handler.LoginFailHandler;
import com.study.ecommerce.jwt.JwtFilter;
import com.study.ecommerce.jwt.JwtUtil;
import com.study.ecommerce.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final ObjectMapper objectMapper;
    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;


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
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/auth/login","auth/signup","/").permitAll()
                        .requestMatchers("/user").hasAnyRole("USER","ADMIN")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                                .anyRequest().authenticated()
                )
                .addFilterBefore(new JwtFilter(jwtUtil), LoginFilter.class)
                .addFilterAt(loginFilter(), UsernamePasswordAuthenticationFilter.class) // JWT 필터 추가
                .exceptionHandling(e -> e.accessDeniedHandler(new Http403Handler(objectMapper))
                                    .authenticationEntryPoint(new Http401Handler(objectMapper))
                )
                .build();
    }


    @Bean
    public LoginFilter loginFilter(){
        LoginFilter filter = new LoginFilter(objectMapper,jwtUtil);
        filter.setAuthenticationManager(authenticationManager());
        filter.setFilterProcessesUrl("/auth/login");
        //filter.setAuthenticationSuccessHandler(new SimpleUrlAuthenticationSuccessHandler("/"));
        filter.setAuthenticationFailureHandler(new LoginFailHandler(objectMapper));

        return filter;
    }

    @Bean
    public AuthenticationManager authenticationManager(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(new CustomUserDetailsService(memberRepository));
        provider.setPasswordEncoder(passwordEncoder());

        return new ProviderManager(provider);
    }





}
