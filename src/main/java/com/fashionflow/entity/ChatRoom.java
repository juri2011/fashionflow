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

    /*
        연관관계를 따로 지정하지 않았습니다. : 상품이나 사용자가 삭제되어도 채팅방을 그대로 유지하기 위해
    */
    private Long itemId;

    private String buyerEmail;

    private String sellerEmail;


    public static ChatRoom createChatRoom(ChatRoomDTO chatRoomDTO){
        return ChatRoom.builder()
                .uuid(chatRoomDTO.getRoomId())
                .name(chatRoomDTO.getName())
                .itemId(chatRoomDTO.getItem().getId())
                .buyerEmail(chatRoomDTO.getBuyer().getEmail())
                .sellerEmail(chatRoomDTO.getSeller().getEmail())
                .build();
    }
}
