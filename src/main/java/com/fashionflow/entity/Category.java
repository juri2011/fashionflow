package com.fashionflow.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {

    @Id
    @Column(name="category_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //카테고리 id

    private String code; //카테고리 코드

    private String name; //카테고리명

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="parent_id")
    private Category parent; //상위 카테고리(자신이 하위 카테고리일때만)

    @Column(nullable = true)
    private Integer listOrder; //하위 메뉴 리스트 순서

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL,fetch = FetchType.LAZY,
            orphanRemoval = true)
    private List<Item> item = new ArrayList<>(); //카테고리 상품 리스트

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL,fetch = FetchType.LAZY,
            orphanRemoval = true)
    private List<Category> children = new ArrayList<>(); //현재 메뉴의 하위 메뉴 리스트

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", listOrder=" + listOrder +
                '}';
    }
}
