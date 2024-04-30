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
public class SecurityConfig{

    /*임시 권한 부여*/
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeRequests(auth -> auth
                .requestMatchers("/**").permitAll() // 루트 URL은 인증 없이 접근 허용
                .requestMatchers("/members/memberEdit").authenticated() // 루트 URL은 인증 없이 접근 허용
                .anyRequest().authenticated());


        http.formLogin(form -> form
                .loginPage("/members/login")
                .defaultSuccessUrl("/", true)
                .failureUrl("/members/login/error")
                .usernameParameter("email")
                .passwordParameter("password")
                .permitAll());

        // 로그아웃 설정 추가
        http.logout(logout -> logout
                .logoutUrl("/members/logout") // 로그아웃 처리 URL
                .logoutSuccessUrl("/") // 로그아웃 성공 후 리다이렉트할 URL
                .invalidateHttpSession(true) // 세션 무효화
                .deleteCookies("JSESSIONID") // 쿠키 삭제
                .permitAll());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

/*
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
    }*/
}