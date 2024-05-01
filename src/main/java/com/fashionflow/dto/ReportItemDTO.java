package com.fashionflow.dto;

import com.fashionflow.constant.ReportStatus;
import com.fashionflow.constant.ReportTagItem;
import com.fashionflow.entity.ReportItem;
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
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportItemDTO {

    private Long id; //상품신고번호

    //private Long reporterMemberId; //신고자 회원번호
    private String reporterMemberEmail; //신고자 회원번호

    //private MemberFormDTO reporterMember;

    @NotNull(message = "상품번호는 필수입력값입니다.")
    private Long reportedItemId; //신고대상 상품번호

    private ItemFormDTO reportedItem;

    private LocalDateTime regdate; //등록일

    @NotBlank(message = "내용은 필수 입력값입니다.")
    private String content; //내용

    private ReportStatus reportStatus; //처리상태

    @Builder.Default
    private List<ReportItemTagDTO> reportItemTagDTOList = new ArrayList<>();

    @Builder.Default
    private List<String> reportTagItemList = new ArrayList<>();

    @Builder.Default
    private boolean isMyReport = false;

    private static ModelMapper modelMapper = new ModelMapper();


    public ReportItem createReportItem(){

        return ReportItem.builder()
                .regdate(LocalDateTime.now())
                .content(this.content)
                .reportStatus(ReportStatus.WAITING)
                .build();
    }
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
