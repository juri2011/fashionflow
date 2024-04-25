package com.fashionflow.entity;

import com.fashionflow.constant.ReportTagMember;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ReportMemberTag {
    @Id
    @Column(name="report_tag_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="report_id", nullable = false)
    private ReportMember reportMember;

    @Enumerated(EnumType.STRING)
    private ReportTagMember reportTagMember;
}
