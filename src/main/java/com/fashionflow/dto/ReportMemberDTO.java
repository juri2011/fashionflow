package com.fashionflow.dto;

import com.fashionflow.constant.ReportStatus;
import com.fashionflow.entity.Member;
import com.fashionflow.entity.ReportMember;
import com.fashionflow.entity.ReportMemberTag;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportMemberDTO {
    private Long id;

    //private Long reporterMemberId;
    private String reporterMemberEmail;

    //private Long reportedMemberId;
    @NotNull(message = "신고대상자 이메일은 필수입력값입니다.")
    private String reportedMemberEmail;

    private MemberFormDTO reportedMember;

    private LocalDateTime regdate;

    @NotBlank(message = "내용은 필수 입력값입니다.")
    private String content;

    private ReportStatus reportStatus;

    private boolean isMyReport;

    @Builder.Default
    private List<ReportMemberTagDTO> reportMemberTagDTOList = new ArrayList<>();

    @Builder.Default
    private List<String> reportMemberTagStringList = new ArrayList<>();

    private static ModelMapper modelMapper = new ModelMapper();


    public ReportMember createReportMember(){
        return ReportMember.builder()
                .regdate(LocalDateTime.now())
                .content(this.content)
                .reportStatus(ReportStatus.WAITING)
                .build();
    }

    public static ReportMemberDTO entityToDTO(ReportMember reportMember){
        ReportMemberDTO reportMemberDTO = modelMapper.map(reportMember, ReportMemberDTO.class);
        //reportMemberDTO.setReporterMemberId(reportMember.getReporterMember().getId());
        reportMemberDTO.setReporterMemberEmail(reportMember.getReporterMember().getEmail());
        //reportMemberDTO.setReportedMemberId(reportMember.getReportedMember().getId());
        reportMemberDTO.setReportedMemberEmail(reportMember.getReportedMember().getEmail());

        return reportMemberDTO;
    }
}
