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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/chat")
@Slf4j
public class RoomController {

    private final ChatRoomRepository chatRoomRepository;

    private final MemberService memberService;

    private final ChatService chatService;

    private final ItemService itemService;

    // 내가 속해있는 채팅방만
    @GetMapping("/flowtalk")
    public String myRoom(@AuthenticationPrincipal User user, Model model) {
        if(memberService.findUnregisteredOAuthMember()){
            return "redirect:/oauth/login";
        }
        String userEmail = memberService.currentMemberEmail();
        if (userEmail.equals("anonymousUser")) {
            return "/error/loginError";
        }

        log.info("# All Chat Rooms");
        List<ChatRoomDTO> sellRooms = new ArrayList<>();
        List<ChatRoomDTO> buyRooms = new ArrayList<>();
        String currentMemberEmail = memberService.currentMemberEmail();
        for(ChatRoom chatRoom : chatRoomRepository.findAllByOrderByIdDesc()){
            if(chatRoom.getSellerEmail() == null || chatRoom.getBuyerEmail()==null) continue;
            //사용자가 속해있는 채팅방만 표시
            if(chatRoom.getSellerEmail().equals(currentMemberEmail) ||
               chatRoom.getBuyerEmail().equals(currentMemberEmail)){
                ChatRoomDTO chatRoomDTO = ChatRoomDTO.entityToDto(chatRoom);
                try{
                    chatRoomDTO.setItem(itemService.getItemDetail(chatRoom.getItemId()));
                }catch(EntityNotFoundException e){
                    chatRoomDTO.setItem(null);
                }
                try{
                    chatRoomDTO.setSeller(memberService.getMemberFormDTOByEmail(chatRoom.getSellerEmail()));
                }catch(EntityNotFoundException e){
                    chatRoomDTO.setSeller(null);
                }
                try{
                    chatRoomDTO.setBuyer(memberService.getMemberFormDTOByEmail(chatRoom.getBuyerEmail()));
                }catch(EntityNotFoundException e){
                    chatRoomDTO.setBuyer(null);
                }
                if(chatRoomDTO.getSeller().getEmail().equals(currentMemberEmail)){
                    sellRooms.add(chatRoomDTO);
                }else if(chatRoomDTO.getBuyer().getEmail().equals(currentMemberEmail)){
                    buyRooms.add(chatRoomDTO);
                }
            }
        }

        System.out.println("==============sell=============");
        sellRooms.forEach(room -> System.out.println("========================" + room));
        System.out.println("==============buy=============");
        buyRooms.forEach(room -> System.out.println("========================" + room));

        model.addAttribute("sellList", sellRooms);
        model.addAttribute("buyList", buyRooms);

        return "chat/rooms";
    }

    //채팅방 개설
    @PostMapping("/room")
    public String createRoom(@RequestParam("itemId") Long itemId,
                             @RequestParam("shopMemberEmail")
                             String shopMemberEmail,
                             RedirectAttributes rttr){
        try{
            // 상품 정보
            ItemFormDTO item = itemService.getItemDetail(itemId);
            // 구매자 정보
            String currentMember = memberService.currentMemberEmail();
            MemberFormDTO buyer = memberService.getMemberFormDTOByEmail(currentMember);
            // 판매자 정보
            MemberFormDTO seller = memberService.getMemberFormDTOByEmail(shopMemberEmail);

            // 자신이 등록한 상품이면
            if(buyer.getEmail().equals(seller.getEmail())){
                log.info("판매자 정보가 구매자 정보와 일치합니다.");
                return "redirect:/";
            }

            //이미 있는 채팅방인지 확인
            if(chatService.checkDuplicateRoom(item, buyer, seller)){
                log.info("이미 존재하는 채팅방");
                ChatRoom chatRoom = chatRoomRepository.findByItemIdAndBuyerEmailAndSellerEmail(
                        item.getId(),buyer.getEmail(), seller.getEmail()
                ).orElseThrow(() ->
                        new EntityNotFoundException("채팅방 정보를 찾을 수 없습니다."));
                return "redirect:/chat/room?roomId="+chatRoom.getUuid();
            }

            //상품 이름으로 채팅방 개설
            log.info("# Create Chat Room , name: " + item.getItemName());

            ChatRoomDTO chatRoomDTO = ChatRoomDTO.create(item.getItemName(), item, buyer, seller);
            ChatRoom chatRoom = ChatRoom.createChatRoom(chatRoomDTO);
            System.out.println(chatRoom);
            ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);
            rttr.addFlashAttribute("roomName", chatRoomDTO);
            return "redirect:/chat/room?roomId="+savedChatRoom.getUuid();

        }catch(EntityNotFoundException e){
            log.info("정보를 불러오는 중에 에러가 발생했습니다. : "+e.getMessage());
            return "redirect:/";

        }catch(Exception e){
            log.info("에러가 발생했습니다. : "+e.getMessage());
            return "redirect:/";
        }
    }
    
    //채팅방 조회
    @GetMapping("/room")
    public String getRoom(@RequestParam("roomId") String roomId, Model model){
        if(memberService.findUnregisteredOAuthMember()){
            return "redirect:/oauth/login";
        }

    private ChatRoomDTO getChatRoomDTO(ChatRoom chatRoom){

        /* entity -> DTO 변환 */
        ChatRoomDTO chatRoomDTO = ChatRoomDTO.entityToDto(chatRoom);

        /* chatRoomDTO 활성화 */
        chatRoomDTO.setEnabled(true);

        /* 상품 정보 */
        ItemFormDTO item;
        MemberFormDTO seller;
        MemberFormDTO buyer;

        /* 상품 정보 */
        try{
            item = itemService.getItemDetail(chatRoom.getItemId());
        }catch(EntityNotFoundException e){
            item = null;
            chatRoomDTO.setEnabled(false);
        }

        /* 판매자 정보  */
        try{
            seller = memberService.getMemberFormDTOByEmail(chatRoom.getSellerEmail());
        }catch(EntityNotFoundException e){
            seller = null;
            chatRoomDTO.setEnabled(false);
        }

        /* 구매자 정보 */
        try{
            buyer = memberService.getMemberFormDTOByEmail(chatRoom.getBuyerEmail());
        }catch(EntityNotFoundException e){
            buyer = null;
            chatRoomDTO.setEnabled(false);
        }

        chatRoomDTO.setItem(item);
        chatRoomDTO.setBuyer(buyer);
        chatRoomDTO.setSeller(seller);

        return chatRoomDTO;
    }

    //채팅방 조회
    @GetMapping("/room")
    public String getRoom(@RequestParam("roomId") String roomId, Model model){

        log.info("# get Chat Room, roomID : " + roomId);

        ChatRoom chatRoom = chatRoomRepository.findByUuid(roomId).orElseThrow(() ->
                new EntityNotFoundException("방이 존재하지 않습니다. roomId = " + roomId));

        ChatRoomDTO chatRoomDTO = getChatRoomDTO(chatRoom);

        /* 현재 접속한 사용자가 채팅방에 들어갈 수 있는지 확인 */
        String currentMember = memberService.currentMemberEmail();
        if(!currentMember.equals(chatRoom.getBuyerEmail()) &&
                !currentMember.equals(chatRoom.getSellerEmail())){
            log.info("허가되지 않은 사용자입니다.");
            return "redirect:/";
        }

        model.addAttribute("room", chatRoomDTO);
        model.addAttribute("currentMember", currentMember);

        return "/chat/room";
    }

    @GetMapping("/getUsername")
    public @ResponseBody ResponseEntity getUsername(){

        try{
            Member member = memberService.findMemberByCurrentEmail();
            String username = member.getNickname();
            String userEmail = member.getEmail();
            MemberFormDTO memberFormDTO = new MemberFormDTO();
            memberFormDTO.setNickname(username);
            memberFormDTO.setEmail(userEmail);
            return new ResponseEntity<>(memberFormDTO, HttpStatus.OK);
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
            return new ResponseEntity<String>("채팅방이 존재하지 않습니다.", HttpStatus.NOT_FOUND);
        }catch (Exception e){
            return new ResponseEntity<String>("대화내용을 불러오는데 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/userValidate")
    public @ResponseBody ResponseEntity userValidate(@RequestBody Map<String, String> requestData){
        System.out.println("===========================validate 진입");

        String roomId = requestData.get("roomId");
        System.out.println(roomId);
        String currentMemberEmail;
        //현재 사용자 이메일
        
        /* 사용자가 로그인하지 않았을 시 예외처리 */
        try{
            currentMemberEmail = memberService.currentMemberEmail();
            if(currentMemberEmail == null){
                return new ResponseEntity<String>("로그인 후 이용해주세요", HttpStatus.UNAUTHORIZED);
            }
        }catch(Exception e){
            return new ResponseEntity<String>("로그인 후 이용해주세요", HttpStatus.UNAUTHORIZED);
        }

        Optional<ChatRoom> chatRoom = chatRoomRepository.findByUuid(roomId);
        System.out.println(chatRoom);
        if(chatRoom.isEmpty()){
            return new ResponseEntity<String>("채팅방을 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
        }

        if(!currentMemberEmail.equals(chatRoom.get().getBuyerEmail()) &&
                !currentMemberEmail.equals(chatRoom.get().getSellerEmail())){
            return new ResponseEntity<String>("허가되지 않은 사용자입니다.", HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<String>("유효한 사용자 확인", HttpStatus.OK);
    }


    @PostMapping("/sell")
    public @ResponseBody ResponseEntity sell(@RequestBody Map<String, String> requestData){
        String roomId = requestData.get("roomId");
        try{
            chatService.sell(roomId);
            return new ResponseEntity<String>("성공적으로 처리되었습니다.", HttpStatus.OK);
        }catch(EntityNotFoundException e){
            return new ResponseEntity<String>("정보를 불러오는 도중 에러가 발생했습니다.", HttpStatus.NOT_FOUND);
        }catch (Exception e){
            return new ResponseEntity<String>("오류가 발생했습니다." + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    @PostMapping("/getroominfo")
    public @ResponseBody ResponseEntity getRoomInfo(@RequestBody Map<String, String> requestData){
        String roomId = requestData.get("roomId");
        ChatRoom chatRoom = chatRoomRepository.findByUuid(roomId).orElseThrow(() ->
                new EntityNotFoundException("방이 존재하지 않습니다. roomId = " + roomId));

        ChatRoomDTO chatRoomDTO = getChatRoomDTO(chatRoom);
        return new ResponseEntity<ChatRoomDTO>(chatRoomDTO, HttpStatus.OK);
    }
}