package com.fashionflow.service;

import com.fashionflow.entity.ReportItem;
import com.fashionflow.repository.ReportItemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
        List<ReportItem> reportItemList = reportItemRepository.findAll();

        reportItemList.forEach(reportItem -> System.out.println("=============================="+reportItem));
    }

}