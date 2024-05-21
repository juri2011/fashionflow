package com.fashionflow.repository;

import com.fashionflow.entity.ChatRoom;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

// 채팅 룸 엔터티에 대한 데이터 액세스를 처리하는 리포지토리
public interface ChatRoomRepository extends CrudRepository<ChatRoom, Long> {

    // ID를 기준으로 내림차순으로 모든 채팅 룸을 가져오는 메서드
    public List<ChatRoom> findAllByOrderByIdDesc();

    // 주어진 UUID에 해당하는 채팅 룸을 가져오는 메서드
    public Optional<ChatRoom> findByUuid(String uuid);

    // 아이템 ID, 구매자 이메일, 판매자 이메일에 해당하는 채팅 룸을 가져오는 메서드
    public Optional<ChatRoom> findByItemIdAndBuyerEmailAndSellerEmail(Long itemId, String buyerEmail, String sellerEmail);
}
