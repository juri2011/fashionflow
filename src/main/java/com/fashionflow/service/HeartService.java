package com.fashionflow.service;

import com.fashionflow.repository.HeartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HeartService {

    private static HeartRepository heartRepository;

    public Long countHeartById(Long itemId){
        return heartRepository.countByItemId(itemId);
    }
}
