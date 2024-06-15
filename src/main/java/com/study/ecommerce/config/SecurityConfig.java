package com.study.ecommerce.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.ecommerce.config.filter.EmailPasswordAuthFilter;
import com.study.ecommerce.config.handler.Http401Handler;
import com.study.ecommerce.config.handler.Http403Handler;
import com.study.ecommerce.config.handler.LoginFailHandler;
import com.study.ecommerce.domain.Member;
import com.study.ecommerce.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
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
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.session.security.web.authentication.SpringSessionRememberMeServices;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final ObjectMapper objectMapper;
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
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/auth/login","auth/signup").permitAll()
                        .requestMatchers("/member").hasAnyRole("USER","ADMIN")
                        //.requestMatchers("/admin").hasRole("ADMIN")
                        .requestMatchers("/admin/**")
                            .access(new WebExpressionAuthorizationManager("hasRole('ADMIN') AND hasAuthority('WRITE')"))
                        .anyRequest().authenticated()
                )
                .addFilterBefore(emailPasswordAuthFilter(), UsernamePasswordAuthenticationFilter.class)
//                .formLogin((form) -> form
//                        .loginPage("/auth/login")
//                        .loginProcessingUrl("/auth/login")
//                        .usernameParameter("email")
//                        .passwordParameter("password")
//                        .defaultSuccessUrl("/")
//                        .failureHandler(new LoginFailHandler(objectMapper))
//                )
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
    public EmailPasswordAuthFilter emailPasswordAuthFilter(){
        EmailPasswordAuthFilter filter = new EmailPasswordAuthFilter("/auth/login",objectMapper);
        SpringSessionRememberMeServices rememberMeServices = new SpringSessionRememberMeServices();
        rememberMeServices.setAlwaysRemember(true);
        rememberMeServices.setValiditySeconds(3600 * 24 * 30);


        filter.setAuthenticationManager(authenticationManager());
        filter.setAuthenticationSuccessHandler(new SimpleUrlAuthenticationSuccessHandler("/"));
        filter.setAuthenticationFailureHandler(new LoginFailHandler(objectMapper));
        filter.setSecurityContextRepository(new HttpSessionSecurityContextRepository());   //세션발급
        filter.setRememberMeServices(rememberMeServices);
        return filter;
    }

    @Bean
    public AuthenticationManager authenticationManager(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService(memberRepository));
        provider.setPasswordEncoder(passwordEncoder());

        return new ProviderManager(provider);
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
