package com.fashionflow.dto;

import com.fashionflow.constant.ReportTagItem;
import com.fashionflow.entity.ReportItemTag;
import lombok.*;
import org.modelmapper.ModelMapper;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportItemTagDTO {
    private Long id; //상품 신고 태그 번호

    private ReportTagItem reportTagItem; //상품 신고 태그 내용

    private String reportTagDesc; //상품 신고 태그 설명(한글)

    private static ModelMapper modelMapper = new ModelMapper();

    public ReportItemTag createReportItemTagDTO(){
        return ReportItemTag.builder()
                .reportTagItem(this.reportTagItem)
                .build();
    }

    public static ReportItemTagDTO entityToDTO(ReportItemTag reportItemTag){
        ReportItemTagDTO reportItemTagDTO = modelMapper.map(reportItemTag, ReportItemTagDTO.class);
        reportItemTagDTO.setReportTagDesc(reportItemTagDTO.getReportTagItem().getDescription());
        return reportItemTagDTO;
    }
}
