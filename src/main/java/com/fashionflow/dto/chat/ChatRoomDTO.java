package com.fashionflow.dto.chat;

import com.fashionflow.dto.ItemImgDTO;
import com.fashionflow.entity.ChatRoom;
import com.fashionflow.entity.ItemImg;
import lombok.*;
import org.modelmapper.ModelMapper;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoomDTO {
    private String roomId;
    private String name;
    private Set<WebSocketSession> sessions = new HashSet<>();

    public static ChatRoomDTO create(String name){
        ChatRoomDTO room = new ChatRoomDTO();

        room.roomId = UUID.randomUUID().toString();
        room.name = name;
        return room;
    }

    public static ChatRoomDTO entityToDto(ChatRoom chatRoom){
        return ChatRoomDTO.builder()
                .roomId(chatRoom.getUuid())
                .name(chatRoom.getName())
                .build();
    }


}
