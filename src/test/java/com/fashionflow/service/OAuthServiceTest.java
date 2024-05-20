package com.fashionflow.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
@ExtendWith(MockitoExtension.class)
class OAuthServiceTest {





    // 난수 문자열 생성 메서드
    public static String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+";
        Random rand = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int randomIndex = rand.nextInt(characters.length());
            char randomChar = characters.charAt(randomIndex);
            stringBuilder.append(randomChar);
        }
        return stringBuilder.toString();
    }

    @Test
    void testGenerateRandomStringLength() {
        int length = 10;
        String randomString = generateRandomString(length);

        // 문자열 길이가 요청한 길이와 같은지 확인
        assertEquals(length, randomString.length(), "Generated string length should match the requested length");
    }

    @Test
    void testGenerateRandomStringCharacters() {
        int length = 20;
        String randomString = generateRandomString(length);
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+";

        // 모든 문자가 지정된 문자 집합에 포함되어 있는지 확인
        for (char c : randomString.toCharArray()) {
            assertTrue(characters.indexOf(c) != -1, "Generated string contains invalid character: " + c);
        }
    }

    @Test
    void testGenerateRandomStringUniqueness() {
        int length = 15;
        String randomString1 = generateRandomString(length);
        String randomString2 = generateRandomString(length);

        // 두 개의 난수 문자열이 동일하지 않은지 확인
        assertTrue(!randomString1.equals(randomString2), "Generated strings should be unique");
    }

}