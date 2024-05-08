package com.fashionflow.entity;

import com.fashionflow.constant.ReportStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportMember {
    @Id
    @Column(name="report_member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="reporter_member")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member reporterMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="reported_member")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member reportedMember;

    @Column(nullable = false)
    private LocalDateTime regdate;

    @Lob
    @Column(nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    private ReportStatus reportStatus;

/*
    @OneToMany(mappedBy = "reportMember", cascade = CascadeType.ALL,fetch = FetchType.LAZY,
            orphanRemoval = true)
    private List<ReportMemberTag> reportMemberTagList;
*/

}
