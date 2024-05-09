package com.fashionflow.common;

import com.fashionflow.dto.chat.ChatMessageDTO;
import com.fashionflow.entity.ChatMessage;
import com.fashionflow.entity.ChatRoom;
import com.fashionflow.repository.ChatMessageRepository;
import com.fashionflow.repository.ChatRoomRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@EnableAsync
@EnableScheduling
@Component
public class MessageBuffer {
    private static final int BUFFER_SIZE = 10; // 버퍼 크기
    private static final long FLUSH_INTERVAL_MS = 5000; // 저장 주기 (밀리초)

    private List<ChatMessage> buffer = new ArrayList<>();

    @Autowired
    private ChatMessageRepository chatMessageRepository;
    @Autowired
    private ChatRoomRepository chatRoomRepository;

    public synchronized void addMessage(ChatMessageDTO message) {
        /* DTO를 entity로 변환 */
        
        //채팅방 entity 가져오기
        ChatRoom chatRoom = chatRoomRepository.findByUuid(message.getRoomId()).orElseThrow(() ->
                new EntityNotFoundException("채팅방이 존재하지 않습니다. id = " + message.getRoomId()));

        //저장할 채팅 entity 생성
        ChatMessage messageToSave = ChatMessage.createChatMessage(chatRoom, message);
        buffer.add(messageToSave);
        if (buffer.size() >= BUFFER_SIZE) {
            flushBuffer();
        }

        System.out.println("==================addMessage진입");
    }

    @Async
    public CompletableFuture<Void> flushBufferAsync() {
        System.out.println("========================flushBufferAsync 실행");
        return CompletableFuture.runAsync(this::flushBuffer);
    }

    @Scheduled(fixedRate = FLUSH_INTERVAL_MS)
    public synchronized void flushBufferAtInterval() {
        System.out.println("========================flushBufferAtInterval 실행");
        if (!buffer.isEmpty()) {
            flushBuffer();
        }
    }

    private void flushBuffer() {
        List<ChatMessage> messagesToSave = new ArrayList<>(buffer);
        buffer.clear();
        chatMessageRepository.saveAll(messagesToSave);
    }
}
