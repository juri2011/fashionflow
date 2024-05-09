package com.fashionflow.repository;

import com.fashionflow.entity.ChatRoom;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends CrudRepository<ChatRoom, Long> {
    public List<ChatRoom> findAllByOrderByIdDesc();

    public Optional<ChatRoom> findByUuid(String uuid);
}
