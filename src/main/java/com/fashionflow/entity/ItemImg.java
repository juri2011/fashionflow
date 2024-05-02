package com.fashionflow.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
public class ItemImg {

    @Id
    @Column(name="item_img_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imgName;      //이미지 파일명

    private String oriImgName;  //원본 이미지 파일

    private String imgUrl;      //조회 경로

    private String repimgYn;     //대표 이미지 여부

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    public void updateItemImg(String oriImgNamem, String imgName, String imgUrl){
        this.oriImgName = oriImgNamem;
        this.imgName = imgName;
        this.imgUrl = imgUrl;
    }
}
