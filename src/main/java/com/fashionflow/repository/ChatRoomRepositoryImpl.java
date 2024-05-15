package com.fashionflow.repository;

import com.fashionflow.dto.ItemFormDTO;
import com.fashionflow.dto.MemberFormDTO;
import com.fashionflow.dto.chat.ChatRoomDTO;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.util.*;

/* DB로 수정할 예정 */
@Repository
public class ChatRoomRepositoryImpl {

    private Map<String, ChatRoomDTO> chatRoomDTOMap;

    @PostConstruct
    private void init(){
        chatRoomDTOMap = new LinkedHashMap<>();
    }

    public List<ChatRoomDTO> findAllRooms(){
        //채팅방 생성 순서 최근 순으로 반환
        List<ChatRoomDTO> result = new ArrayList<>(chatRoomDTOMap.values());
        Collections.reverse(result);

        return result;
    }

    public ChatRoomDTO findRoomById(String id){
        return chatRoomDTOMap.get(id);
    }

    public ChatRoomDTO createChatRoomDTO(String name, ItemFormDTO itemFormDTO, MemberFormDTO sender, MemberFormDTO receiver) {
        ChatRoomDTO room = ChatRoomDTO.create(name, itemFormDTO, sender, receiver);
        chatRoomDTOMap.put(room.getRoomId(), room);

        return room;
    }

}