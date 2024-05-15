package com.fashionflow.dto.chat;

import com.fashionflow.dto.ItemFormDTO;
import com.fashionflow.dto.ItemImgDTO;
import com.fashionflow.dto.MemberFormDTO;
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

    private ItemFormDTO item;
    private MemberFormDTO seller;
    private MemberFormDTO buyer;

    private boolean enabled;

    @Builder.Default
    private Set<WebSocketSession> sessions = new HashSet<>();

    public static ChatRoomDTO create(String name, ItemFormDTO item, MemberFormDTO buyer, MemberFormDTO seller){
        return ChatRoomDTO.builder()
                .name(name)
                .item(item)
                .buyer(buyer)
                .seller(seller)
                .roomId(UUID.randomUUID().toString())
                .build();
    }

    public static ChatRoomDTO entityToDto(ChatRoom chatRoom){
        return ChatRoomDTO.builder()
                .roomId(chatRoom.getUuid())
                .name(chatRoom.getName())
                .build();
    }


}
