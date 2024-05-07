package com.fashionflow.service;

import com.fashionflow.entity.Heart;
import com.fashionflow.repository.HeartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HeartService {

    private final HeartRepository heartRepository;

    public Long countHeartById(Long itemId){
        return heartRepository.countByItemId(itemId);
    }

    public void addHeart(Long itemId, String name) {
        //사용자가 찜한 항목?
        List<Heart> heartList = heartRepository.findByMember_Email(name);
    }
}
