package com.fashionflow.service;

import com.fashionflow.constant.SellStatus;
import com.fashionflow.dto.ItemFormDTO;
import com.fashionflow.dto.MemberFormDTO;
import com.fashionflow.dto.chat.ChatMessageDTO;
import com.fashionflow.dto.chat.ChatRoomDTO;
import com.fashionflow.entity.*;
import com.fashionflow.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomRepository chatRoomRepository; // 채팅방 저장소
    private final ChatMessageRepository chatMessageRepository; // 채팅 메시지 저장소

    private final ItemRepository itemRepository; // 상품 저장소
    private final MemberRepository memberRepository; // 회원 저장소
    private final ItemBuyRepository itemBuyRepository; // 구매 저장소
    private final ItemSellRepository itemSellRepository; // 판매 저장소

    public List<ChatMessageDTO> getChatHistory(String uuid) {
        ChatRoom chatRoom = chatRoomRepository.findByUuid(uuid).orElseThrow(() ->
                new EntityNotFoundException("채팅방이 존재하지 않습니다. uuid = " + uuid)); // 채팅방 찾기

        List<ChatMessage> chatMessageList = chatMessageRepository.findByChatRoomIdOrderByIdAsc(chatRoom.getId()); // 채팅 메시지 목록 찾기

        List<ChatMessageDTO> chatHistory = new ArrayList<>();

        for (ChatMessage chatMessage : chatMessageList) {
            ChatMessageDTO chatMessageDTO = ChatMessageDTO.entityToDTO(chatMessage); // DTO로 변환
            chatHistory.add(chatMessageDTO); // 채팅 기록에 추가
        }

        return chatHistory; // 채팅 기록 반환
    }

    public boolean checkDuplicateRoom(ItemFormDTO itemFormDTO,
                                      MemberFormDTO buyer,
                                      MemberFormDTO seller) {

        Long itemId = itemFormDTO.getId(); // 상품 ID
        String buyerEmail = buyer.getEmail(); // 구매자 이메일
        String sellerEmail = seller.getEmail(); // 판매자 이메일

        Optional<ChatRoom> chatRoom =
                chatRoomRepository.findByItemIdAndBuyerEmailAndSellerEmail(itemId, buyerEmail, sellerEmail); // 중복 채팅방 찾기
        return chatRoom.isPresent(); // 존재 여부 반환
    }

    public void sell(String roomId) {
        ChatRoom chatRoom = chatRoomRepository.findByUuid(roomId).orElseThrow(() ->
                new EntityNotFoundException("방이 존재하지 않습니다. roomId = " + roomId)); // 채팅방 찾기

        Member seller = memberRepository.findByEmail(chatRoom.getSellerEmail()); // 판매자 찾기
        if (seller == null) {
            throw new EntityNotFoundException("사용자가 존재하지 않습니다. sellerEmail = " + chatRoom.getSellerEmail());
        }

        Member buyer = memberRepository.findByEmail(chatRoom.getBuyerEmail()); // 구매자 찾기
        if (buyer == null) {
            throw new EntityNotFoundException("사용자가 존재하지 않습니다. buyerEmail = " + chatRoom.getBuyerEmail());
        }

        Item item = itemRepository.findById(chatRoom.getItemId()).orElseThrow(() ->
                new EntityNotFoundException("상품이 존재하지 않습니다. id = " + chatRoom.getItemId())); // 상품 찾기

        // 판매 완료 처리
        item.setSellStatus(SellStatus.SOLD_OUT);

        // 판매 목록에 저장
        ItemSell itemSell = ItemSell.builder()
                .item(item)
                .member(seller)
                .sellDate(LocalDateTime.now())
                .build();
        itemSellRepository.save(itemSell);

        // 구매 목록에 저장
        ItemBuy itemBuy = ItemBuy.builder()
                .item(item)
                .member(buyer)
                .buyDate(LocalDateTime.now())
                .build();
        itemBuyRepository.save(itemBuy);
    }
}
