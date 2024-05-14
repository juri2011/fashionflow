package com.fashionflow.controller;

import com.fashionflow.service.HeartService;
import com.fashionflow.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class HeartController {
    private final HeartService heartService;

    @PostMapping("/heart/addHeart")
    public @ResponseBody ResponseEntity addHeart(@RequestBody Map<String, Long> requestData, Principal principal){
        Long itemId = requestData.get("id");
        System.out.println("상품번호 : " + itemId);
        String msg = null;
        try{
            System.out.println("사용자 이메일 : " + principal.getName());
            msg = heartService.addHeart(itemId, principal.getName());
        }catch(NullPointerException e){
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }catch(Exception e){
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }


        //사용자가 이미 찜한 상품인지 확인
        //사용자가 찜하지 않았으면 찜 목록에 추가
        //현재 상품에 찜 횟수 추가

        return new ResponseEntity<String>(msg, HttpStatus.OK);
    }

    @PostMapping("/heart/removeHeart/{heartId}")
    public String removeHeart(@PathVariable("heartId") Long heartId, Principal principal, RedirectAttributes redirectAttributes) {
        String userEmail = principal.getName();
        try {
            // 찜 항목 삭제
            heartService.removeHeart(heartId, userEmail);
            // 삭제 후 /myshop 페이지로 리다이렉트
            return "redirect:/myshop";
        } catch (IllegalArgumentException e) {
            // 해당 찜을 삭제할 수 없는 경우에 대한 처리
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/myshop";
        } catch (Exception e) {
            // 삭제 중 오류가 발생한 경우에 대한 처리
            redirectAttributes.addFlashAttribute("errorMessage", "찜 항목 삭제 중 오류가 발생했습니다.");
            return "redirect:/myshop";
        }
    }
}
