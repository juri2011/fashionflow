package com.fashionflow.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
@ExtendWith(MockitoExtension.class)
class OAuthServiceTest {


    @Autowired
    public OAuthService oAuthService;

    @Test
    void testGenerateRandomStringCharacters() {
        int length = 20;
        String randomString = oAuthService.generateRandomString(length);
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+";

        // 모든 문자가 지정된 문자 집합에 포함되어 있는지 확인
        for (char c : randomString.toCharArray()) {
            assertTrue(characters.indexOf(c) != -1, "Generated string contains invalid character: " + c);
        }
    }

    @Test
    void testGenerateRandomStringUniqueness() {
        int length = 15;
        String randomString1 = oAuthService.generateRandomString(length);
        String randomString2 = oAuthService.generateRandomString(length);

        // 두 개의 난수 문자열이 동일하지 않은지 확인
        assertTrue(!randomString1.equals(randomString2), "Generated strings should be unique");
    }

}