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

    /* 임시 권한 부여 */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // 권한 설정
        // 권한 설정
        http.authorizeRequests(auth -> auth
                .requestMatchers("/chart","/report/itemdetail/**","/report/memberdetail/**").hasRole("ADMIN") // "/chart" 경로는 ADMIN 역할을 가진 사용자만 접근 가능
                .requestMatchers("/**").permitAll() // 루트 URL은 인증 없이 접근 허용
                .anyRequest().authenticated()); // 다른 모든 요청은 인증 필요

        // OAuth2.0 설정
        http.oauth2Login(oauth2Login -> oauth2Login
                .loginPage("/members/login") // 사용자 지정 로그인 페이지 경로
                .defaultSuccessUrl("/oauth/login", true) // OAuth 로그인 성공 후 리다이렉션될 경로
                .failureUrl("/login")); // 로그인 실패 시 리다이렉션될 경로

        // 폼 로그인 설정
        http.formLogin(form -> form
                .loginPage("/members/login") // 로그인 페이지 경로
                .defaultSuccessUrl("/", true) // 로그인 성공 후 리다이렉션될 경로
                .failureUrl("/members/login/error") // 로그인 실패 시 리다이렉션될 경로
                .usernameParameter("email") // 사용자 이름 파라미터
                .passwordParameter("password") // 비밀번호 파라미터
                .permitAll()); // 로그인 페이지는 모든 사용자에게 접근 허용

        // 로그아웃 설정 추가
        http.logout(logout -> logout
                .logoutUrl("/members/logout") // 로그아웃 처리 URL
                .logoutSuccessUrl("/") // 로그아웃 성공 후 리다이렉트할 URL
                .invalidateHttpSession(true) // 세션 무효화
                .deleteCookies("JSESSIONID", "recentViewedItems") // 쿠키 삭제
                .permitAll()); // 로그아웃 URL은 모든 사용자에게 접근 허용

        return http.build(); // SecurityFilterChain 반환
    }

    // 비밀번호 인코더 빈 설정
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // BCryptPasswordEncoder 반환
    }
}