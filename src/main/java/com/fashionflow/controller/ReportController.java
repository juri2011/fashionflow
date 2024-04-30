package com.fashionflow.controller;

import com.fashionflow.dto.ReportItemDTO;
import com.fashionflow.dto.ReportItemTagDTO;
import com.fashionflow.dto.ReportMemberDTO;
import com.fashionflow.entity.ReportMember;
import com.fashionflow.service.ReportItemService;
import com.fashionflow.service.ReportMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

// 신고 컨트롤러
@Controller
@RequiredArgsConstructor
public class ReportController {

    private final ReportItemService reportItemService;
    private final ReportMemberService reportMemberService;
    @GetMapping("/report")
    public String report(Model model){

        List<ReportItemDTO> reportItemDTOList = reportItemService.getReportItemDTOList();
        List<ReportMemberDTO> reportMemberDTOList = reportMemberService.getReportMemberDTOList();

        //reportItemDTOList.forEach(reportItemDTO -> System.out.println("==========================" + reportItemDTO));
        reportMemberDTOList.forEach(reportMemberDTO -> System.out.println("==========================" + reportMemberDTO));

        //model.addAttribute("reportItemList", reportItemDTOList);
        //model.addAttribute("reportMemberList", reportMemberDTOList)
        return "report/reportList";
    }
}
