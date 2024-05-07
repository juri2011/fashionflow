package com.fashionflow.controller;

import com.fashionflow.service.HeartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class HeartController {
    private final HeartService heartService;

    
    //작업중
    @PostMapping("/heart/addHeart")
    public @ResponseBody ResponseEntity addHeart(@RequestBody Map<String, Long> requestData, Principal principal){
        Long id = requestData.get("id");
        System.out.println("상품번호 : " + id);
        /*
        try{
            System.out.println("사용자 이메일 : " + principal.getName());
        }catch(Exception e){
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
        */
        //사용자가 이미 찜한 상품인지 확인

        return new ResponseEntity<Long>(1L, HttpStatus.OK);
    }
}
