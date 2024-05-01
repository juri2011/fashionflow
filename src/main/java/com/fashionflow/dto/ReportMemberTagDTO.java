package com.fashionflow.dto;

import com.fashionflow.constant.ReportTagMember;
import com.fashionflow.entity.ReportMemberTag;
import lombok.*;
import org.modelmapper.ModelMapper;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportMemberTagDTO {
    private Long id;

    private ReportTagMember reportTagMember;

    private static ModelMapper modelMapper = new ModelMapper();

    public static ReportMemberTagDTO entityToDTO(ReportMemberTag reportMemberTag){
        return modelMapper.map(reportMemberTag, ReportMemberTagDTO.class);
    }
}
