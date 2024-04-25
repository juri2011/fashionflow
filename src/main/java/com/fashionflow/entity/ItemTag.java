package com.fashionflow.entity;

import com.fashionflow.constant.ItemTagName;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="item_tag")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemTag {

    @Id
    @Column(name="item_tag_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private ItemTagName itemTagName;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="item_id")
    private Item item;
}
