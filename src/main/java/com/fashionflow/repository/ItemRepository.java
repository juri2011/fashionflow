package com.fashionflow.repository;

import com.fashionflow.entity.Item;
import com.fashionflow.entity.Member;
import com.fashionflow.entity.ProfileImage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long>, JpaSpecificationExecutor<Item> {


    public List<Item> findByMemberId(Long memberId);

    public List<Item> findByMemberOrderByRegdateDesc(Member member);

    public Item findItemById(Long id);

    public List<Item> findTop8ByOrderByViewCountDesc(Pageable pageable);
}
