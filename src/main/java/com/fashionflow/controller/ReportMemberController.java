package com.fashionflow.controller;

import com.fashionflow.dto.ReportCommandDTO;
import com.fashionflow.dto.ReportItemDTO;
import com.fashionflow.dto.ReportMemberDTO;
import com.fashionflow.service.ReportItemService;
import com.fashionflow.service.ReportMemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ReportMemberController {

    //private final ReportItemService reportItemService;
    private final ReportMemberService reportMemberService;
/*
    @GetMapping("/report/itemdetail/{id}")
    public String reportDetail(Model model, @PathVariable("id") Long id){
        ReportItemDTO reportItemDTO = reportItemService.getReportItemDTOById(id);
        System.out.println(reportItemDTO);
        model.addAttribute("reportItem",reportItemDTO);
        return "report/reportItemDetail";
    }*/

    @GetMapping({"/report/member", "/report/member/{page}"})
    public String reportNew(Model model, Principal principal,
                            @PathVariable("page") Optional<Integer> page){

        System.out.println("현재 페이지 ================="+page.orElse(0));

        /* 경로에 페이지 번호가 없으면 0페이지 조회 */
        Pageable pageable = PageRequest.of(page.orElse(0), 2);
        Page<ReportMemberDTO> reportMembers = reportMemberService.getReportMemberDTOPage(pageable);

        //로그인한 사용자에 대해 자신이 작성한 리뷰항목 구분
        if(principal != null){
            //사용자가 작성한 신고 항목 구분
            reportMembers.getContent().forEach(reportMemberDTO -> {
                if(principal.getName().equals(reportMemberDTO.getReporterMemberEmail())){
                    reportMemberDTO.setMyReport(true);
                }
            });
        }

        reportMembers.getContent().forEach(reportItemDTO -> System.out.println("================"+reportItemDTO));

        System.out.println("총 페이지 수 : "+reportMembers.getTotalPages());

        model.addAttribute("reportMemberList", reportMembers);
        model.addAttribute("maxPage", 10); //페이지네이션 한 블록당 출력할 페이지 수


        return "report/reportMemberList";
    }

/*
    @PutMapping("/reportItem/update")
    public @ResponseBody ResponseEntity updateReportItem(@RequestBody @Valid ReportItemDTO reportItemDTO,
                                                         BindingResult bindingResult,
                                                         Principal principal){
        System.out.println("=========================put 진입");
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
            reportItemId = reportItemService.updateReportItem(reportItemDTO);
        } catch(Exception e){
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Long>(reportItemId, HttpStatus.OK);

    }

    @GetMapping("/reportItem/{id}")
    public @ResponseBody ResponseEntity get(@PathVariable("id") Long id){
        System.out.println(id);
        ReportItemDTO reportItemDTO = reportItemService.getReportItemDTOById(id);
        return new ResponseEntity<>(reportItemDTO,HttpStatus.OK);
    }

    @DeleteMapping("/delete/reportItem/{id}")
    public @ResponseBody ResponseEntity deleteReportItem(@PathVariable("id") Long id){

        System.out.println("========================== delete :"+id);

        try {
            reportItemService.deleteReportItem(id);
            return new ResponseEntity<>("성공적으로 삭제되었습니다.", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("삭제 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
        return new ResponseEntity<Long>(reportItemId, HttpStatus.OK);

    }

    @PostMapping("/report/process")
    public @ResponseBody ResponseEntity processItem(@RequestBody ReportCommandDTO reportCommandDTO){

        ReportItemDTO reportItemDTO = reportItemService.getReportItemDTOById(reportCommandDTO.getId());
        try {
            reportItemService.processReportItem(reportItemDTO, reportCommandDTO.getCommand());
        } catch(Exception e){
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Long>(reportCommandDTO.getId(), HttpStatus.OK);
    }*/
}
