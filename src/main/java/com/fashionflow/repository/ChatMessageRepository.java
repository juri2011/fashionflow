package com.fashionflow.repository;

import com.fashionflow.entity.ChatMessage;
import com.fashionflow.entity.ReportItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends CrudRepository<ChatMessage, Long> {

    public List<ChatMessage> findByChatRoomIdOrderByIdAsc(Long roomId);
}
