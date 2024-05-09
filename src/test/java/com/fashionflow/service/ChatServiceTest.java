package com.fashionflow.service;

import com.fashionflow.dto.chat.ChatMessageDTO;
import com.fashionflow.entity.ChatMessage;
import com.fashionflow.entity.ChatRoom;
import com.fashionflow.repository.ChatMessageRepository;
import com.fashionflow.repository.ChatRoomRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ChatServiceTest {

    @Autowired
    ChatRoomRepository chatRoomRepository;

    @Autowired
    ChatMessageRepository chatMessageRepository;

    public List<ChatMessageDTO> getChatHistory(String uuid){
        ChatRoom chatRoom = chatRoomRepository.findByUuid(uuid).orElseThrow(() ->
                new EntityNotFoundException("채팅방이 존재하지 않습니다. uuid = " + uuid));

        List<ChatMessage> chatMessageList = chatMessageRepository.findByChatRoomIdOrderByIdAsc(chatRoom.getId());

        List<ChatMessageDTO> chatHistory = new ArrayList<>();

        for(ChatMessage chatMessage : chatMessageList){
            ChatMessageDTO chatMessageDTO = ChatMessageDTO.entityToDTO(chatMessage);
            chatHistory.add(chatMessageDTO);
        }

        return chatHistory;
    }

    @Test
    public void printChatHistory(){
        String uuid = "44a48077-5719-4563-88b8-c80828e54f00";
        List<ChatMessageDTO> chatHistory = getChatHistory(uuid);
        chatHistory.forEach(chat -> System.out.println("================" + chat));
    }
}