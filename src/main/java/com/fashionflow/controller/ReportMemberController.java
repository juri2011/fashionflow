package com.fashionflow.controller;

import com.fashionflow.dto.ReportCommandDTO;
import com.fashionflow.dto.ReportItemDTO;
import com.fashionflow.dto.ReportMemberDTO;
import com.fashionflow.service.MemberService;
import com.fashionflow.service.ReportItemService;
import com.fashionflow.service.ReportMemberService;
import jakarta.persistence.EntityNotFoundException;
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

    private final ReportMemberService reportMemberService;
    private final MemberService memberService;

    @GetMapping("/report/memberdetail/{id}")
    public String reportDetail(Model model, @PathVariable("id") Long id){
        if(memberService.findUnregisteredOAuthMember()){
            return "redirect:/oauth/login";
        }
        ReportMemberDTO reportMemberDTO = reportMemberService.getReportMemberDTOById(id);
        System.out.println(reportMemberDTO);
        model.addAttribute("reportMember",reportMemberDTO);
        return "report/reportMemberDetail";
    }

    @PostMapping("/report/reportMember")
    public @ResponseBody ResponseEntity reportMember(@RequestBody @Valid ReportMemberDTO reportMemberDTO,
                                                     BindingResult bindingResult){

        System.out.println(reportMemberDTO);

        if(bindingResult.hasErrors()){
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();

            for(FieldError fieldError : fieldErrors){
                sb.append(fieldError.getDefaultMessage());
            }

            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
        }

        if(memberService.currentMemberEmail() == null || memberService.currentMemberEmail().equals("anonymousUser"))
            return new ResponseEntity<String>("로그인이 필요합니다.", HttpStatus.BAD_REQUEST);
        String email = memberService.currentMemberEmail();
        System.out.println(email);

        reportMemberDTO.setReporterMemberEmail(email);
        System.out.println(reportMemberDTO);
        Long reportItemId;

        try {
            reportItemId = reportMemberService.addReportMember(reportMemberDTO);
        } catch(EntityNotFoundException e) {
            //로그인 정보가 없는 상태에서 DB에 저장하려 할 때 에러 메시지 출력
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch(Exception e){
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Long>(reportItemId, HttpStatus.OK);

    }

    @GetMapping({"/report/member", "/report/member/{page}"})
    public String reportNew(Model model, Principal principal,
                            @PathVariable("page") Optional<Integer> page){
        if(memberService.findUnregisteredOAuthMember()){
            return "redirect:/oauth/login";
        }

        System.out.println("현재 페이지 ================="+page.orElse(0));

        /* 경로에 페이지 번호가 없으면 0페이지 조회 */
        Pageable pageable = PageRequest.of(page.map(integer -> integer - 1).orElse(0), 5);
        //Pageable pageable = PageRequest.of(page.orElse(0), 5);
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

    //사용자 신고 정보 가져오기
    @GetMapping("/reportMember/{id}")
    public @ResponseBody ResponseEntity get(@PathVariable("id") Long id){

        System.out.println(id);
        ReportMemberDTO reportMemberDTO = reportMemberService.getReportMemberDTOById(id);
        return new ResponseEntity<>(reportMemberDTO,HttpStatus.OK);
    }

    @DeleteMapping("/delete/reportMember/{id}")
    public @ResponseBody ResponseEntity deleteReportMember(@PathVariable("id") Long id){

        System.out.println("========================== delete :"+id);

        try {
            reportMemberService.deleteReportMember(id);
            return new ResponseEntity<>("성공적으로 삭제되었습니다.", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("삭제 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/reportMember/update")
    public @ResponseBody ResponseEntity updateReportItem(@RequestBody @Valid ReportMemberDTO reportMemberDTO,
                                                         BindingResult bindingResult,
                                                         Principal principal){
        if(bindingResult.hasErrors()){
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();

            for(FieldError fieldError : fieldErrors){
                sb.append(fieldError.getDefaultMessage());
            }

            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
        }
        if(memberService.currentMemberEmail() == null || memberService.currentMemberEmail().equals("anonymousUser"))
            return new ResponseEntity<String>("로그인이 필요합니다.", HttpStatus.BAD_REQUEST);
        String email = memberService.currentMemberEmail();
        System.out.println(email);

        reportMemberDTO.setReporterMemberEmail(email);
        System.out.println(reportMemberDTO);
        Long reportMemberId;


        try {
            reportMemberId = reportMemberService.updateReportMember(reportMemberDTO);
        } catch(Exception e){
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Long>(reportMemberId, HttpStatus.OK);

    }

    @PostMapping("/report/processMember")
    public @ResponseBody ResponseEntity processMember(@RequestBody ReportCommandDTO reportCommandDTO){

        ReportMemberDTO reportMemberDTO = reportMemberService.getReportMemberDTOById(reportCommandDTO.getId());
        try {
            reportMemberService.processReportMember(reportMemberDTO, reportCommandDTO.getCommand());
        } catch(Exception e){
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Long>(reportCommandDTO.getId(), HttpStatus.OK);
    }
}
