package com.fashionflow.repository;

import com.fashionflow.constant.SellStatus;
import com.fashionflow.entity.Item;
import com.fashionflow.entity.Member;
import com.fashionflow.entity.ProfileImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

// 아이템에 대한 데이터 액세스를 처리하는 리포지토리
public interface ItemRepository extends JpaRepository<Item, Long>, JpaSpecificationExecutor<Item> {

    // 특정 회원 ID에 해당하는 아이템 목록을 가져오는 메서드
    public List<Item> findByMemberId(Long memberId);

    // 특정 회원에 속하는 아이템을 등록 날짜 내림차순으로 가져오는 메서드
    public List<Item> findByMemberOrderByRegdateDesc(Member member);

    // 주어진 판매 상태에 해당하는 상위 8개의 아이템을 조회수 내림차순으로 가져오는 메서드
    List<Item> findTop8BySellStatusOrderByViewCountDesc(SellStatus sellStatus, Pageable pageable);

    // 주어진 ID에 해당하는 아이템을 찾는 메서드
    public Item findItemById(Long id);

    // 특정 회원에 속하는 아이템을 등록 날짜 내림차순으로 가져오는 메서드
    Page<Item> findByMemberOrderByRegdateDesc(Member member, Pageable pageable);
}
