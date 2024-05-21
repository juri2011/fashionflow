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

    // 메시지를 저장할 버퍼
    private List<ChatMessage> buffer = new ArrayList<>();

    // ChatMessageRepository 및 ChatRoomRepository를 자동으로 주입
    @Autowired
    private ChatMessageRepository chatMessageRepository;
    @Autowired
    private ChatRoomRepository chatRoomRepository;

    // 메세지 버퍼 추가
    public synchronized void addMessage(ChatMessageDTO message) {
        // DTO를 entity로 변환

        // 채팅방 entity 가져오기
        ChatRoom chatRoom = chatRoomRepository.findByUuid(message.getRoomId()).orElseThrow(() ->
                new EntityNotFoundException("채팅방이 존재하지 않습니다. id = " + message.getRoomId()));

        // 저장할 채팅 entity 생성
        ChatMessage messageToSave = ChatMessage.createChatMessage(chatRoom, message);
        buffer.add(messageToSave);

        // 버퍼 크기가 최대 크기 이상이면 flushBuffer 호출
        if (buffer.size() >= BUFFER_SIZE) {
            flushBuffer();
        }

        System.out.println("==================addMessage진입");
    }

    // 비동기 버퍼 초기화
    @Async
    public CompletableFuture<Void> flushBufferAsync() {
        System.out.println("========================flushBufferAsync 실행");
        return CompletableFuture.runAsync(this::flushBuffer);
    }


    // 일정 시간 간격으로 버퍼 초기화
    @Scheduled(fixedRate = FLUSH_INTERVAL_MS)
    public synchronized void flushBufferAtInterval() {
        // 버퍼가 비어 있지 않으면 flushBuffer 호출
        if (!buffer.isEmpty()) {
            flushBuffer();
        }
    }


    // 버퍼의 모든 메시지를 데이터베이스에 저장하고 버퍼 초기화
    private void flushBuffer() {
        // 버퍼의 메시지 목록을 새 리스트에 복사
        List<ChatMessage> messagesToSave = new ArrayList<>(buffer);
        // 버퍼 비우기
        buffer.clear();
        // 메시지 저장
        chatMessageRepository.saveAll(messagesToSave);
    }
}