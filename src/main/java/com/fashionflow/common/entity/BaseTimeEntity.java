package com.fashionflow.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EntityListeners(value = {AuditingEntityListener.class})
@MappedSuperclass
@Getter
@Setter
public class BaseTimeEntity {

    // 엔터티가 생성될 때 등록 날짜를 저장, 업데이트 불가능
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime regDate;

    // 엔터티가 마지막으로 수정될 때 수정 날짜를 저장
    @LastModifiedDate
    private LocalDateTime updateDate;
}
