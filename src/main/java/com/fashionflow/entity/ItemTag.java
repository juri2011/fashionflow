package com.fashionflow.entity;

import com.fashionflow.constant.ItemTagName;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
    @Enumerated(EnumType.STRING)
    private ItemTagName itemTagName;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="item_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Item item;

    public ItemTag(ItemTagName itemTagName, Item item) {
        this.itemTagName = itemTagName;
        this.item = item;
    }
}
