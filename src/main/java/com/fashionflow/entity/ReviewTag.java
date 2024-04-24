package com.fashionflow.entity;

import com.fashionflow.constant.ReviewTagContent;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ReviewTag {

    @Id
    @Column(name="review_tag_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="review_id")
    private Review review;

    @Enumerated(EnumType.STRING)
    private ReviewTagContent reviewTagContent;
}
