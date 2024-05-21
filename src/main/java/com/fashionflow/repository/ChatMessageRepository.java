package com.fashionflow.repository;

import com.fashionflow.entity.ChatMessage;
import com.fashionflow.entity.ReportItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// 채팅 메시지 엔터티에 대한 데이터 액세스를 처리하는 리포지토리
@Repository
public interface ChatMessageRepository extends CrudRepository<ChatMessage, Long> {

    // 주어진 채팅 룸 ID에 해당하는 메시지를 ID의 오름차순으로 가져오는 메서드
    public List<ChatMessage> findByChatRoomIdOrderByIdAsc(Long roomId);
}