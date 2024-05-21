package com.fashionflow.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(value = {AuditingEntityListener.class})
@MappedSuperclass
@Getter
@Setter
public abstract class BaseEntity extends BaseTimeEntity{

    // 엔터티가 생성될 때 생성자를 저장, 업데이트 불가능
    @CreatedBy
    @Column(updatable = false)
    private String createBy;

    // 엔터티가 마지막으로 수정될 때 수정자를 저장
    @LastModifiedBy
    private String modifiedBy;
}
