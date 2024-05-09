package com.fashionflow.entity;

import com.fashionflow.dto.chat.ChatRoomDTO;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatRoom {

    @Id
    @Column(name="room_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String uuid; //채팅방 일련번호

    private String name; //채팅방 이름

    public static ChatRoom createChatRoom(ChatRoomDTO chatRoomDTO){
        return ChatRoom.builder()
                .uuid(chatRoomDTO.getRoomId())
                .name(chatRoomDTO.getName())
                .build();
    }
}
