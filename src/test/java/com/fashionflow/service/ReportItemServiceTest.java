package com.fashionflow.service;

import com.fashionflow.dto.ReportItemDTO;
import com.fashionflow.dto.ReportItemTagDTO;
import com.fashionflow.entity.ReportItem;
import com.fashionflow.entity.ReportItemTag;
import com.fashionflow.repository.ReportItemRepository;
import com.fashionflow.repository.ReportItemTagRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ReportItemServiceTest {
    @Autowired
    ReportItemRepository reportItemRepository;

    @Autowired
    ReportItemTagRepository reportItemTagRepository;


    @Test
    public void findReportTest(){
        //List<ReportItem> reportItemList = reportItemRepository.findAll();
        List<ReportItem> reportItemList = reportItemRepository.findAllByOrderByRegdateDesc();
        reportItemList.forEach(reportItem -> System.out.println("=============================="+reportItem));
    }

    //페이징이 적용된 리스트 출력 테스트
    @Test
    public void findReportTest2(){
        Pageable pageable = PageRequest.of(2, 2); //0페이지부터 시작
        Page<ReportItem> reportItems = reportItemRepository.findAll(pageable);
        //Page<ReportItem> reportItems = reportItemRepository.getReportItemPage(pageable);

        reportItems.getContent().forEach(reportItem -> System.out.println("================" + reportItem));

        System.out.println("페이지수? :"+reportItems.getTotalPages());

        /*
        Page<ReportItemDTO> reportItemDTOPage = reportItems.map(m -> {

            //신고 항목 entity를 DTO로 변환
            ReportItemDTO reportItemDTO = ReportItemDTO.entityToDTO(m);

            *//* 신고 태그 목록 필드에 들어갈 리스트 생성 *//*
            //DB에서 현재 신고목록의 태그 목록 받아오기
            List<ReportItemTag> reportItemTagList = reportItemTagRepository.findAllByReportItem_Id(m.getId());

            //신고 항목 DTO에 저장할 리스트 생성
            List<ReportItemTagDTO> reportItemTagDTOList = new ArrayList<>();

            //신고 태그 목록 Entity -> DTO로 변경
            for(ReportItemTag reportItemTag : reportItemTagList){
                ReportItemTagDTO reportItemTagDTO = ReportItemTagDTO.entityToDTO(reportItemTag);
                reportItemTagDTOList.add(reportItemTagDTO);
            }

            //신고 항목의 태그목록 필드 저장
            reportItemDTO.setReportItemTagDTOList(reportItemTagDTOList);

            return reportItemDTO;
        });
        */

        //reportItemDTOPage.getContent().forEach(reportItem -> System.out.println("================" + reportItem));
    }

}