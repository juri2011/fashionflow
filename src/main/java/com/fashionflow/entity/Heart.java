package com.fashionflow.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Heart {
    @Id
    @Column(name="heart_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="item_id")
    //@OnDelete(action = OnDeleteAction.CASCADE)
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    //@OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;
}
