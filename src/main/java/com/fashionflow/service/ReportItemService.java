package com.fashionflow.service;

import com.fashionflow.dto.ReportItemDTO;
import com.fashionflow.entity.ReportItem;
import com.fashionflow.repository.ReportItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportItemService {
    public final ReportItemRepository reportItemRepository;

    public List<ReportItemDTO> getReportItemDTOList(){
        
        /* Repository로부터 List 받아오기 */
        List<ReportItem> reportItemList = reportItemRepository.findAllByOrderByRegdateDesc();

        List<ReportItemDTO> reportItemDTOList = new ArrayList<>();

        return null;
    }
}
