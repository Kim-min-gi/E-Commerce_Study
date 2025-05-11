package com.study.ecommerce.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.ecommerce.config.filter.LoginFilter;
import com.study.ecommerce.config.handler.Http401Handler;
import com.study.ecommerce.config.handler.Http403Handler;
import com.study.ecommerce.config.handler.LoginFailHandler;
import com.study.ecommerce.config.jwt.JwtFilter;
import com.study.ecommerce.config.jwt.JwtUtil;
import com.study.ecommerce.repository.token.BlackListTokenRepository;
import com.study.ecommerce.repository.token.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

//    private final MemberRepository memberRepository;

    private final ObjectMapper objectMapper;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    private final BlackListTokenRepository blackListTokenRepository;


    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){ //spring security 아예 안타게
        return new WebSecurityCustomizer() {
            @Override
            public void customize(WebSecurity web) {
                web.ignoring().requestMatchers("/favicon.ico","/error")
                        .requestMatchers("/css/**", "/js/**", "/images/**")
                        .requestMatchers(PathRequest.toH2Console());
            }
        };
    }

    public static final List<String> PERMIT_ALL_URIS = List.of(
            "/", "/cartPage", "/signUpPage", "/loginPage",
            "/auth/login", "/auth/signup", "/auth/signup/admin", "/auth/reissue","/productPage","/orderPage",
            "/payment/**"
    );



    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtUtil jwtUtil) throws Exception{


        http.csrf(AbstractHttpConfigurer::disable);

        http.formLogin(AbstractHttpConfigurer::disable);
        http.httpBasic(AbstractHttpConfigurer::disable);

        http.authorizeHttpRequests(request -> request
                .requestMatchers(PERMIT_ALL_URIS.toArray(String[]::new)).permitAll()
                .requestMatchers("/user").hasAnyRole("USER", "ADMIN")
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
        );


        http.sessionManagement((session) -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(new JwtFilter(jwtUtil, blackListTokenRepository), UsernamePasswordAuthenticationFilter.class);
        //http.addFilterBefore(new JwtFilter(jwtUtil,blackListTokenRepository), LoginFilter.class);
        http.addFilterAt(loginFilter(), UsernamePasswordAuthenticationFilter.class);

        http.exceptionHandling(e -> e.accessDeniedHandler(new Http403Handler(objectMapper))
                .authenticationEntryPoint(new Http401Handler(objectMapper))
        );

        return http.build();

    }



    //AuthenticationManager Bean 등록
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {

        return configuration.getAuthenticationManager();
    }



    @Bean
    public LoginFilter loginFilter() throws Exception {
        AuthenticationManager authenticationManager = authenticationManager(authenticationConfiguration);
        LoginFilter filter = new LoginFilter(objectMapper,jwtUtil,"/auth/login",refreshTokenRepository);
        filter.setAuthenticationManager(authenticationManager);
        filter.setAuthenticationFailureHandler(new LoginFailHandler(objectMapper));

        return filter;
    }





}
