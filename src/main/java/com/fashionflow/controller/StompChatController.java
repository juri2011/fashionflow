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

    /**
     * 사용자가 채팅방에 입장할 때 호출되는 메서드.
     * '/pub/chat/enter' 경로로 메시지를 보내면 이 메서드가 실행됩니다.
     *
     * @param message 채팅 메시지 DTO, 사용자가 보낸 메시지 정보가 포함되어 있습니다.
     */
    @MessageMapping(value = "/chat/enter")
    public void enter(ChatMessageDTO message){
        // 사용자가 채팅방에 참여했음을 알리는 메시지를 설정합니다.
        message.setMessage(message.getWriter() + "님이 채팅방에 참여하였습니다.");
        // 해당 채팅방 구독자들에게 메시지를 전송합니다.
        template.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
    }

    /**
     * 사용자가 채팅방에 메시지를 보낼 때 호출되는 메서드.
     * '/pub/chat/message' 경로로 메시지를 보내면 이 메서드가 실행됩니다.
     *
     * @param message 채팅 메시지 DTO, 사용자가 보낸 메시지 정보가 포함되어 있습니다.
     */
    @MessageMapping(value = "/chat/message")
    public void message(ChatMessageDTO message){
        // 메시지를 버퍼에 추가합니다.
        messageBuffer.addMessage(message);
        // 해당 채팅방 구독자들에게 메시지를 전송합니다.
        template.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
    }
}
