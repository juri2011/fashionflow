package com.fashionflow.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="profile_image")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileImage {

    @Id
    @Column(name="profile_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    private String uploadPath;
    private String uuid;
    private String filename;
}
