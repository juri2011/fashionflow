package com.fashionflow.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(auth -> auth
                        .requestMatchers("/**").permitAll() // 모든 경로에 대해 인증 없이 접근 허용
                        .anyRequest().authenticated() // 그 외 모든 요청은 인증 필요
                )
                .formLogin(form -> form
                        .loginPage("/members/login") // 로그인 페이지 경로 설정
                        .defaultSuccessUrl("/", true) // 로그인 성공 후 리다이렉트할 기본 경로
                        .failureUrl("/members/login/error") // 로그인 실패 시 리다이렉트할 경로
                        .usernameParameter("email") // username 파라미터를 email로 사용
                        .passwordParameter("password") // password 파라미터 사용
                        .permitAll() // 모든 사용자가 로그인 페이지 접근 허용
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
