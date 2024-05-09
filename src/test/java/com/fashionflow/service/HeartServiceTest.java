package com.fashionflow.service;

import com.fashionflow.entity.Heart;
import com.fashionflow.repository.HeartRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class HeartServiceTest {

    @Autowired
    HeartRepository heartRepository;

    @Test
    public void findHeartByMemberEmail(){
        List<Heart> heartList = heartRepository.findByMember_Email("juri@naver.com");
        heartList.forEach(heart -> System.out.println(heartList));
    }
}