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

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final ItemBuyRepository itemBuyRepository;
    private final ItemSellRepository itemSellRepository;

    public List<ChatMessageDTO> getChatHistory(String uuid){
        ChatRoom chatRoom = chatRoomRepository.findByUuid(uuid).orElseThrow(() ->
                new EntityNotFoundException("채팅방이 존재하지 않습니다. uuid = " + uuid));

        List<ChatMessage> chatMessageList = chatMessageRepository.findByChatRoomIdOrderByIdAsc(chatRoom.getId());

        List<ChatMessageDTO> chatHistory = new ArrayList<>();

        for(ChatMessage chatMessage : chatMessageList){
            ChatMessageDTO chatMessageDTO = ChatMessageDTO.entityToDTO(chatMessage);
            chatHistory.add(chatMessageDTO);
        }

        return chatHistory;
    }

    public boolean checkDuplicateRoom(ItemFormDTO itemFormDTO,
                                      MemberFormDTO buyer,
                                      MemberFormDTO seller){

        Long itemId = itemFormDTO.getId();
        String buyerEmail = buyer.getEmail();
        String sellerEmail = seller.getEmail();

        Optional<ChatRoom> chatRoom =
                chatRoomRepository.findByItemIdAndBuyerEmailAndSellerEmail(itemId, buyerEmail, sellerEmail);
        return chatRoom.isPresent();
    }

    public void sell(String roomId){
        ChatRoom chatRoom = chatRoomRepository.findByUuid(roomId).orElseThrow(() ->
                new EntityNotFoundException("방이 존재하지 않습니다. roomId = " + roomId));

        Member seller = memberRepository.findByEmail(chatRoom.getSellerEmail());
        if(seller == null){
            throw new EntityNotFoundException("사용자가 존재하지 않습니다. sellerEmail = " + chatRoom.getSellerEmail());
        }

        Member buyer = memberRepository.findByEmail(chatRoom.getBuyerEmail());
        if(buyer == null){
            throw new EntityNotFoundException("사용자가 존재하지 않습니다. buyerEmail = " + chatRoom.getBuyerEmail());
        }

        Item item = itemRepository.findById(chatRoom.getItemId()).orElseThrow(() ->
                new EntityNotFoundException("상품이 존재하지 않습니다. id = " + chatRoom.getItemId()));

        //판매완료 처리
        item.setSellStatus(SellStatus.SOLD_OUT);
        
        //판매목록에 저장
        ItemSell itemSell = ItemSell.builder()
                .item(item)
                .member(seller)
                .sellDate(LocalDateTime.now())
                .build();
        itemSellRepository.save(itemSell);
        
        //구매목록에 저장
        ItemBuy itemBuy = ItemBuy.builder()
                .item(item)
                .member(buyer)
                .buyDate(LocalDateTime.now())
                .build();
        itemBuyRepository.save(itemBuy);
    }
}
