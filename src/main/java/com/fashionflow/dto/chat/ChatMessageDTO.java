package com.fashionflow.dto.chat;

import com.fashionflow.entity.ChatMessage;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageDTO {
    private String roomId;
    private String writer;
    private String message;
    //private LocalDateTime msgDate;

    public static ChatMessageDTO entityToDTO(ChatMessage chatMessage){
        return ChatMessageDTO.builder()
                .roomId(chatMessage.getChatRoom().getUuid())
                .writer(chatMessage.getSender())
                .message(chatMessage.getMessage())
                .build();
    }
}
