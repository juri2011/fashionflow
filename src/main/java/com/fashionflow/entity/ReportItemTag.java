package com.fashionflow.entity;

import com.fashionflow.constant.ReportTagItem;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportItemTag {
    @Id
    @Column(name="report_tag_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="report_id", nullable = false)
    private ReportItem reportItem;

    @Enumerated(EnumType.STRING)
    private ReportTagItem reportTagItem;
}
