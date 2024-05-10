package com.fashionflow.controller;


import com.fashionflow.dto.ItemFormDTO;
import com.fashionflow.dto.MemberFormDTO;
import com.fashionflow.dto.chat.ChatMessageDTO;
import com.fashionflow.dto.chat.ChatRoomDTO;
import com.fashionflow.entity.ChatRoom;
import com.fashionflow.entity.Item;
import com.fashionflow.entity.Member;
import com.fashionflow.repository.ChatRoomRepository;
import com.fashionflow.service.ChatService;
import com.fashionflow.service.ItemService;
import com.fashionflow.service.MemberService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/chat")
@Slf4j
public class RoomController {

    private final ChatRoomRepository chatRoomRepository;

    private final MemberService memberService;

    private final ChatService chatService;

    private final ItemService itemService;

    //채팅방 목록 조회
    @GetMapping("/rooms")
    public String rooms(Model model){

        log.info("# All Chat Rooms");
        List<ChatRoomDTO> rooms = new ArrayList<>();
        for(ChatRoom chatRoom : chatRoomRepository.findAllByOrderByIdDesc()){
            rooms.add(ChatRoomDTO.entityToDto(chatRoom));
        }
        model.addAttribute("list", rooms);

        return "chat/rooms";
    }

    //채팅방 개설
    /*@PostMapping("/room")
    public String create(@RequestParam("name") String name, RedirectAttributes rttr){

        log.info("# Create Chat Room , name: " + name);
        ChatRoomDTO chatRoomDTO = ChatRoomDTO.create(name);
        ChatRoom chatRoom = ChatRoom.createChatRoom(chatRoomDTO);
        System.out.println(chatRoom);
        chatRoomRepository.save(chatRoom);
        rttr.addFlashAttribute("roomName", chatRoomDTO);
        return "redirect:/chat/rooms";
    }
    */
    //채팅방 개설
    @PostMapping("/room")
    public String createRoom(@RequestParam("itemId") Long itemId,
                             @RequestParam("shopMemberEmail")
                             String shopMemberEmail,
                             RedirectAttributes rttr){

        /* 상품, 판매자, 구매자 - 이미 있는지 확인 */

        ItemFormDTO item = itemService.getItemDetail(itemId);
        String currentMember = null;
        try {
            currentMember = memberService.currentMemberEmail();
        }catch(Exception e){
            log.info("현재 사용자 정보를 불러오는 중에 에러가 발생했습니다. : "+e.getMessage());
            return "redirect:/";

        }
        try{
            MemberFormDTO buyer = memberService.getMemberFormDTOByEmail(currentMember);
            MemberFormDTO seller = memberService.getMemberFormDTOByEmail(shopMemberEmail);

            if(buyer.getEmail().equals(seller.getEmail())){
                log.info("판매자 정보가 구매자 정보와 일치합니다.");
                return "redirect:/";
            }

            //상품 이름으로 채팅방 개설
            log.info("# Create Chat Room , name: " + item.getItemName());

            ChatRoomDTO chatRoomDTO = ChatRoomDTO.create(item.getItemName(), item, buyer, seller);
            ChatRoom chatRoom = ChatRoom.createChatRoom(chatRoomDTO);
            System.out.println(chatRoom);
            chatRoomRepository.save(chatRoom);
            rttr.addFlashAttribute("roomName", chatRoomDTO);
            return "redirect:/chat/rooms";

        }catch(EntityNotFoundException e){
            log.info("사용자 정보를 불러오는 중에 에러가 발생했습니다. : "+e.getMessage());
            return "redirect:/";

        }catch(Exception e){
            log.info("에러가 발생했습니다. : "+e.getMessage());
            return "redirect:/";
        }
    }

    //채팅방 조회
    @GetMapping("/room")
    public void getRoom(@RequestParam("roomId") String roomId, Model model){

        log.info("# get Chat Room, roomID : " + roomId);

        ChatRoom chatRoom = chatRoomRepository.findByUuid(roomId).orElseThrow(() ->
                new EntityNotFoundException("방이 존재하지 않습니다. roomId = " + roomId));

        ChatRoomDTO chatRoomDTO = ChatRoomDTO.entityToDto(chatRoom);

        model.addAttribute("room", chatRoomDTO);
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



    /* 채팅 내역 불러오기 */
    @GetMapping("/getChatHistory/{uuid}")
    public @ResponseBody ResponseEntity getChatHistory(@PathVariable("uuid") String uuid){
        System.out.println(uuid);
        try {
            List<ChatMessageDTO> chatHistory = chatService.getChatHistory(uuid);
            return new ResponseEntity<List<ChatMessageDTO>>(chatHistory, HttpStatus.OK);
        }catch (EntityNotFoundException e){
            return new ResponseEntity<String>("채팅방이 존재하지 않습니다.", HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            return new ResponseEntity<String>("대화내용을 불러오는데 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}