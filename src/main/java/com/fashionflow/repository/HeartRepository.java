package com.fashionflow.repository;

import com.fashionflow.entity.Heart;
import com.fashionflow.entity.Qna;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

// 하트 엔터티에 대한 데이터 액세스를 처리하는 리포지토리
public interface HeartRepository extends JpaRepository<Heart, Long> {

    // 주어진 아이템 ID에 해당하는 하트의 수를 세는 메서드
    public Long countByItemId(Long itemId);

    // 주어진 이메일에 해당하는 회원이 누른 하트 목록을 가져오는 메서드
    public List<Heart> findByMember_Email(String email);

    // 주어진 이메일과 아이템 ID에 해당하는 하트를 찾는 메서드
    Optional<Heart> findByMember_EmailAndItem_Id(String email, Long itemId);
}