package com.fashionflow.dto;

import com.fashionflow.constant.ReportStatus;
import com.fashionflow.entity.ReportItem;
import lombok.*;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportItemDTO {
    private Long id; //상품신고번호

    //private Long reporterMemberId; //신고자 회원번호
    private String reporterMemberEmail; //신고자 회원번호

    //private MemberFormDTO reporterMember;

    //private Long reportedItemId; //신고대상 상품번호

    private ItemFormDTO reportedItem;

    private LocalDateTime regdate; //등록일

    private String content; //내용

    private ReportStatus reportStatus; //처리상태

    @Builder.Default
    private List<ReportItemTagDTO> reportItemTagDTOList = new ArrayList<>();

    private static ModelMapper modelMapper = new ModelMapper();

    public static ReportItemDTO entityToDTO(ReportItem reportItem){
        ReportItemDTO reportItemDTO = modelMapper.map(reportItem, ReportItemDTO.class);

        reportItemDTO.setReportedItem(ItemFormDTO.of(reportItem.getReportedItem()));
        //reportItemDTO.setReportedItemId(reportItem.getReportedItem().getId());
        //reportItemDTO.setReporterMember(MemberFormDTO.entityToDTOSafe(reportItem.getReporterMember()));
        //reportItemDTO.setReporterMemberId(reportItem.getReporterMember().getId());
        reportItemDTO.setReporterMemberEmail(reportItem.getReporterMember().getEmail());

        return reportItemDTO;
    }
}
