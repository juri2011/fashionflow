package com.fashionflow.entity;

import com.fashionflow.constant.ReportStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportItem {
    @Id
    @Column(name="report_item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="reporter_member_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member reporterMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="reported_item_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Item reportedItem;


    @Column(nullable = false)
    private LocalDateTime regdate;

    @Lob
    @Column(nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    private ReportStatus reportStatus;

}
