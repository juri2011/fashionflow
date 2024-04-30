package com.fashionflow.controller;

import com.fashionflow.service.ReportItemService;
import com.fashionflow.service.ReportMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

// 신고 컨트롤러
@Controller
@RequiredArgsConstructor
public class ReportController {

    private final ReportItemService reportItemService;
    private final ReportMemberService reportMemberService;
    @GetMapping("/report")
    public String report(Model model){

        //model.addAttribute("reportItemList", reportItemDTOList);
        //model.addAttribute("reportMemberList", reportMemberDTOList)
        return "report/reportList";
    }
}
