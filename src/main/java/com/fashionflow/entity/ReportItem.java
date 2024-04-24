package com.fashionflow.entity;

import com.fashionflow.constant.ReportStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class ReportItem {
    @Id
    @Column(name="report_item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="reporter_member_id")
    private Member reporterMember;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="reported_item_id")
    private Item reportedItem;

    @Column(nullable = false)
    private LocalDateTime regdate;

    @Lob
    @Column(nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    private ReportStatus reportStatus;

}
