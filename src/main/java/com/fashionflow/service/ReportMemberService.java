package com.fashionflow.service;

import com.fashionflow.dto.ReportItemDTO;
import com.fashionflow.dto.ReportItemTagDTO;
import com.fashionflow.dto.ReportMemberDTO;
import com.fashionflow.dto.ReportMemberTagDTO;
import com.fashionflow.entity.ReportItem;
import com.fashionflow.entity.ReportItemTag;
import com.fashionflow.entity.ReportMember;
import com.fashionflow.entity.ReportMemberTag;
import com.fashionflow.repository.ReportMemberRepository;
import com.fashionflow.repository.ReportMemberTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportMemberService {

    private final ReportMemberRepository reportMemberRepository;
    private final ReportMemberTagRepository reportMemberTagRepository;

    public List<ReportMemberDTO> getReportMemberDTOList(){

        /* Repository로부터 List 받아오기 */
        List<ReportMember> reportMemberList = reportMemberRepository.findAllByOrderByRegdateDesc();

        // view에 전달할 DTO 리스트 생성
        List<ReportMemberDTO> reportMemberDTOList = new ArrayList<>();
        for(ReportMember reportMember : reportMemberList){
            //Entity를 DTO로 변환하여 저장
            ReportMemberDTO reportMemberDTO = ReportMemberDTO.entityToDTO(reportMember);

            //해당 리뷰의 태그 리스트 가져오기
            List<ReportMemberTag> reportMemberTagList = reportMemberTagRepository.findAllByReportMember_Id(reportMember.getId());
            //reportItemList의 reportItemTagDTOList안에 들어갈 List 생성
            List<ReportMemberTagDTO> reportMemberTagDTOList = new ArrayList<>();
            //DTO로 변환
            for(ReportMemberTag reportMemberTag : reportMemberTagList){
                ReportMemberTagDTO reportMemberTagDTO = ReportMemberTagDTO.entityToDTO(reportMemberTag);
                reportMemberTagDTOList.add(reportMemberTagDTO);
            }

            reportMemberDTO.setReportMemberTagDTOList(reportMemberTagDTOList);
            reportMemberDTOList.add(reportMemberDTO);
        }

        return reportMemberDTOList;
    }


}
