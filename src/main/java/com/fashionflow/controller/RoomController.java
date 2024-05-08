package com.fashionflow.controller;


import com.fashionflow.dto.chat.ChatRoomDTO;
import com.fashionflow.entity.Member;
import com.fashionflow.repository.ChatRoomRepository;
import com.fashionflow.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/chat")
@Slf4j
public class RoomController {

    private final ChatRoomRepository repository;

    private final MemberService memberService;

    //채팅방 목록 조회
    @GetMapping("/rooms")
    public String rooms(Model model){

        log.info("# All Chat Rooms");
        model.addAttribute("list", repository.findAllRooms());

        return "chat/rooms";
    }

    //채팅방 개설
    @PostMapping("/room")
    public String create(@RequestParam("name") String name, RedirectAttributes rttr){

        log.info("# Create Chat Room , name: " + name);
        rttr.addFlashAttribute("roomName", repository.createChatRoomDTO(name));
        return "redirect:/chat/rooms";
    }

    //채팅방 조회
    @GetMapping("/room")
    public void getRoom(@RequestParam("roomId") String roomId, Model model){

        log.info("# get Chat Room, roomID : " + roomId);

        ChatRoomDTO chatRoomDTO =repository.findRoomById(roomId);
        log.info("{}",chatRoomDTO);

        model.addAttribute("room", repository.findRoomById(roomId));
    }



    @GetMapping("/getUsername")
    public @ResponseBody ResponseEntity getUsername(){
        try{
            Member member = memberService.findMemberByCurrentEmail();
            String username = member.getNickname();
            log.info(username);
            return new ResponseEntity<String>(username, HttpStatus.OK);
        }catch(NullPointerException e){
            return new ResponseEntity<String>("로그인 후 이용해주세요", HttpStatus.UNAUTHORIZED);
        }
    }
}