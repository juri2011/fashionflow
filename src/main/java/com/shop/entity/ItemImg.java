package com.shop.entity;

import com.shop.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemImg extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_img_id")
    private Long id;

    private String imgName;     //이미지 파일명

    private String oriImgName;     //원본 이미지 파일명

    private String imgUri;     //이미지 조회 경로

    private String repimgYn;     //대표 이미지 파일명

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    public void updateItemImg(String oriImgName, String imgName, String imgUri){
        this.oriImgName = oriImgName;
        this.imgName = imgName;
        this.imgUri = imgUri;
    }
}
