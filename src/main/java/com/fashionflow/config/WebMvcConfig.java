package com.fashionflow.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    // 프로퍼티 파일에서 uploadPath 값을 읽어옴
    @Value("@{uploadPath}")
    String uploadPath;

    @Override
    // 정적 자원 처리를 위한 핸들러를 등록
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // /images/** 경로로 요청이 들어오면 uploadPath 경로의 리소스를 제공
        registry.addResourceHandler("/images/**")
                .addResourceLocations(uploadPath);// uploadPath 경로를 파일 시스템 경로로 설정
    }

}
