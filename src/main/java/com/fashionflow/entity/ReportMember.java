package com.fashionflow.entity;

import com.fashionflow.constant.ReportStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class ReportMember {
    @Id
    @Column(name="report_member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="reporter_member")
    private Member reporterMember;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="reported_member")
    private Member reportedMember;

    @Column(nullable = false)
    private LocalDateTime regdate;

    @Lob
    @Column(nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    private ReportStatus reportStatus;

    @OneToMany(mappedBy = "reportMember", cascade = CascadeType.ALL,fetch = FetchType.LAZY,
            orphanRemoval = true)
    private List<ReportMemberTag> reportMemberTagList;

}
