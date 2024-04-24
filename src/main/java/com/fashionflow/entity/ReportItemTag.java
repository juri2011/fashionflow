package com.fashionflow.entity;

import com.fashionflow.constant.ReportTagItem;
import com.fashionflow.constant.ReportTagMember;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ReportItemTag {
    @Id
    @Column(name="report_tag_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="report_id")
    @Column(nullable = false)
    private ReportMember reportMember;

    @Enumerated(EnumType.STRING)
    private ReportTagItem reportTagItem;
}
