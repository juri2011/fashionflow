package com.fashionflow.entity;

import com.fashionflow.constant.ItemStatus;
import com.fashionflow.constant.SellStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Item {

    @Id
    @Column(name="item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //상품번호

    @Column(nullable = false)
    private String itemName; //상품이름

    @Column(columnDefinition = "longtext", nullable = false)
    private String content; //상세설명

    @Column(nullable = false)
    private int price; //상품가격

    @Column(nullable = false)
    private int delivery; //배송비

    @Column(nullable = false)
    private String address; //주소

    @Column(nullable = false)
    private LocalDateTime regdate; //등록일

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="category_id")
    private Category category; //카테고리

    @Enumerated(EnumType.STRING)
    private ItemStatus itemStatus; //상품 상태

    @Enumerated(EnumType.STRING)
    private SellStatus sellStatus; //판매 상태
}
