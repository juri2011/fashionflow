package com.fashionflow.repository;

import com.fashionflow.dto.ItemFormDTO;
import com.fashionflow.dto.MemberFormDTO;
import com.fashionflow.dto.chat.ChatRoomDTO;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.util.*;

/* DB로 수정할 예정 */
// 채팅 룸 리포지토리 구현체
@Repository
public class ChatRoomRepositoryImpl {

    // 채팅 룸을 저장하는 맵
    private Map<String, ChatRoomDTO> chatRoomDTOMap;

    // 초기화 메서드
    @PostConstruct
    private void init(){
        chatRoomDTOMap = new LinkedHashMap<>();
    }

    // 모든 채팅 룸을 생성 순서의 역순으로 반환하는 메서드
    public List<ChatRoomDTO> findAllRooms(){
        // 채팅방 생성 순서 최근 순으로 반환
        List<ChatRoomDTO> result = new ArrayList<>(chatRoomDTOMap.values());
        Collections.reverse(result);

        return result;
    }

    // 주어진 ID에 해당하는 채팅 룸을 반환하는 메서드
    public ChatRoomDTO findRoomById(String id){
        return chatRoomDTOMap.get(id);
    }

    // 새로운 채팅 룸을 생성하고 맵에 추가하는 메서드
    public ChatRoomDTO createChatRoomDTO(String name, ItemFormDTO itemFormDTO, MemberFormDTO sender, MemberFormDTO receiver) {
        ChatRoomDTO room = ChatRoomDTO.create(name, itemFormDTO, sender, receiver);
        chatRoomDTOMap.put(room.getRoomId(), room);

        return room;
    }
}