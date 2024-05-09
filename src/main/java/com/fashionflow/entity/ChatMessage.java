package com.fashionflow.entity;

import com.fashionflow.dto.chat.ChatMessageDTO;
import com.fashionflow.dto.chat.ChatRoomDTO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessage {

    @Id
    @Column(name="chat_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ChatRoom chatRoom;

    private String sender;

    @Column(columnDefinition = "TEXT")
    private String message;

    private LocalDateTime sendDate;

    public static ChatMessage createChatMessage(ChatRoom chatRoom, ChatMessageDTO chatMessageDTO){

        return ChatMessage.builder()
                .chatRoom(chatRoom)
                .sender(chatMessageDTO.getWriter())
                .message(chatMessageDTO.getMessage())
                .sendDate(LocalDateTime.now())
                .build();
    }


}
