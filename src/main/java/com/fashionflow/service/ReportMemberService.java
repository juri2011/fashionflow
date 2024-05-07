package com.fashionflow.service;

import com.fashionflow.dto.ReportMemberDTO;
import com.fashionflow.dto.ReportMemberTagDTO;
import com.fashionflow.dto.ReportMemberDTO;
import com.fashionflow.dto.ReportMemberTagDTO;
import com.fashionflow.entity.ReportMember;
import com.fashionflow.entity.ReportMemberTag;
import com.fashionflow.entity.ReportMember;
import com.fashionflow.entity.ReportMemberTag;
import com.fashionflow.repository.ReportMemberRepository;
import com.fashionflow.repository.ReportMemberTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportMemberService {

    private final ReportMemberRepository reportMemberRepository;
    private final ReportMemberTagRepository reportMemberTagRepository;

    public Page<ReportMemberDTO> getReportMemberDTOPage(Pageable pageable){


        System.out.println("=====================service==================");
        Page<ReportMember> reportMembers = reportMemberRepository.findAllByOrderByIdDesc(pageable);

        System.out.println(reportMembers.getTotalPages());

        /* 상품 이미지, 태그 정보는 들어있지 않음 */
        Page<ReportMemberDTO> reportMemberDTOPage = reportMembers.map(m -> {

            //신고 항목 entity를 DTO로 변환
            ReportMemberDTO reportMemberDTO = ReportMemberDTO.entityToDTO(m);

            /* 신고 태그 목록 필드에 들어갈 리스트 생성 */
            //DB에서 현재 신고목록의 태그 목록 받아오기
            List<ReportMemberTag> reportMemberTagList = reportMemberTagRepository.findAllByReportMember_Id(m.getId());

            //신고 항목 DTO에 저장할 리스트 생성
            List<ReportMemberTagDTO> reportMemberTagDTOList = new ArrayList<>();

            //신고 태그 목록 Entity -> DTO로 변경
            for(ReportMemberTag reportMemberTag : reportMemberTagList){
                ReportMemberTagDTO reportMemberTagDTO = ReportMemberTagDTO.entityToDTO(reportMemberTag);
                reportMemberTagDTOList.add(reportMemberTagDTO);
            }

            //신고 항목의 태그목록 필드 저장
            reportMemberDTO.setReportMemberTagDTOList(reportMemberTagDTOList);

            return reportMemberDTO;
        });

        System.out.println(reportMemberDTOPage.getTotalPages());


        return reportMemberDTOPage;
    }


}
