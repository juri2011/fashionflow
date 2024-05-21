package com.fashionflow.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    private String imgName;

    private String oriImgName;

    private String imgUrl;


    public void updateProfileImage(String oriImgName, String imgName, String imgUrl){
        this.oriImgName = oriImgName;
        this.imgName = imgName;
        this.imgUrl = imgUrl;
    }

}
