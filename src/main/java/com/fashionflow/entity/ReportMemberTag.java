package com.fashionflow.entity;

import com.fashionflow.constant.ReportTagMember;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportMemberTag {
    @Id
    @Column(name="report_tag_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="report_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ReportMember reportMember;

    @Enumerated(EnumType.STRING)
    private ReportTagMember reportTagMember;
}
