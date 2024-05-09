package com.fashionflow.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    @Bean // JSON 직렬화 & 역직렬화
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
