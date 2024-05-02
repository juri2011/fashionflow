package com.fashionflow.service;

import com.fashionflow.entity.ReportItem;
import com.fashionflow.repository.ReportItemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ReportItemServiceTest {
    @Autowired
    ReportItemRepository reportItemRepository;

    @Test
    public void findReportTest(){
        //List<ReportItem> reportItemList = reportItemRepository.findAll();
        List<ReportItem> reportItemList = reportItemRepository.findAllByOrderByRegdateDesc();
        reportItemList.forEach(reportItem -> System.out.println("=============================="+reportItem));
    }

    //페이징이 적용된 리스트 출력 테스트
    @Test
    public void findReportTest2(){
        Pageable pageable = PageRequest.of(1, 2); //0페이지부터 시작
        Page<ReportItem> reportItems = reportItemRepository.getReportItemPage(pageable);
        reportItems.getContent().forEach(reportItem -> System.out.println("================" + reportItem));
    }

}