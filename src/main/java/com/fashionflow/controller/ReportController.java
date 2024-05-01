package com.fashionflow.controller;

import com.fashionflow.dto.ReportItemDTO;
import com.fashionflow.dto.ReportItemTagDTO;
import com.fashionflow.dto.ReportMemberDTO;
import com.fashionflow.entity.ReportMember;
import com.fashionflow.service.ReportItemService;
import com.fashionflow.service.ReportMemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.List;

// 신고 컨트롤러
@Controller
@RequiredArgsConstructor
public class ReportController {

    private final ReportItemService reportItemService;
    private final ReportMemberService reportMemberService;
    @GetMapping("/report")
    public String report(Model model, Principal principal){

        List<ReportItemDTO> reportItemDTOList = reportItemService.getReportItemDTOList();
        List<ReportMemberDTO> reportMemberDTOList = reportMemberService.getReportMemberDTOList();

        //reportItemDTOList.forEach(reportItemDTO -> System.out.println("==========================" + reportItemDTO));
        reportItemDTOList.forEach(reportItemDTO -> {
            if(principal.getName().equals(reportItemDTO.getReporterMemberEmail())){
                reportItemDTO.setMyReport(true);
            }
            System.out.println("==========================" + reportItemDTO);
        });

        model.addAttribute("reportItemList", reportItemDTOList);
        //model.addAttribute("reportMemberList", reportMemberDTOList);
        return "report/reportList";
    }

    @PostMapping("/reportItem")
    public @ResponseBody ResponseEntity reportItem(@RequestBody @Valid ReportItemDTO reportItemDTO, BindingResult bindingResult,
                                                   Principal principal){
        System.out.println("=========================post 진입");
        if(bindingResult.hasErrors()){
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();

            for(FieldError fieldError : fieldErrors){
                sb.append(fieldError.getDefaultMessage());
            }

            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
        }
        if(principal == null) return new ResponseEntity<String>("로그인이 필요합니다.", HttpStatus.BAD_REQUEST);
        String email = principal.getName();
        System.out.println(email);

        reportItemDTO.setReporterMemberEmail(email);
        System.out.println(reportItemDTO);
        Long reportItemId;

        try {
            reportItemId = reportItemService.addReportItem(reportItemDTO);
        } catch(Exception e){
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Long>(1L, HttpStatus.OK);
        //return new ResponseEntity<Long>(reportItemId, HttpStatus.OK);

    }
}
