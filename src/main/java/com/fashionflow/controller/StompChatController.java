package com.fashionflow.controller;


import com.fashionflow.common.MessageBuffer;
import com.fashionflow.dto.chat.ChatMessageDTO;
import com.fashionflow.dto.chat.ChatRoomDTO;
import com.fashionflow.entity.ChatMessage;
import com.fashionflow.entity.ChatRoom;
import com.fashionflow.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class StompChatController {

    private final SimpMessagingTemplate template; //특정 Broker로 메세지를 전달

    private final MessageBuffer messageBuffer;

    //사용자가 입장하면 '/pub/chat/enter' 경로로 send할 수 있음
    @MessageMapping(value = "/chat/enter")
    public void enter(ChatMessageDTO message){
        message.setMessage(message.getWriter() + "님이 채팅방에 참여하였습니다.");
        template.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
    }

    @MessageMapping(value = "/chat/message")
    public void message(ChatMessageDTO message){
        messageBuffer.addMessage(message);
        template.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
    }
}