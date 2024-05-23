package com.fashionflow.controller;

import com.fashionflow.dto.ReportCommandDTO;
import com.fashionflow.dto.ReportItemDTO;
import com.fashionflow.service.MemberService;
import com.fashionflow.service.ReportItemService;
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

// 신고 컨트롤러
@Controller
@RequiredArgsConstructor
public class ReportItemController {

    private final ReportItemService reportItemService;
    private final MemberService memberService;

    // 특정 신고 항목의 상세 정보를 조회하는 메서드
    @GetMapping("/report/itemdetail/{id}")
    public String reportDetail(Model model, @PathVariable("id") Long id){
        // 만약 OAuth로 등록되지 않은 회원이라면 로그인 페이지로 리디렉션
        if(memberService.findUnregisteredOAuthMember()){
            return "redirect:/oauth/login";
        }
        // 신고 항목 ID를 기반으로 신고 항목 DTO를 조회
        ReportItemDTO reportItemDTO = reportItemService.getReportItemDTOById(id);
        System.out.println(reportItemDTO);
        // 모델에 신고 항목 DTO를 추가
        model.addAttribute("reportItem", reportItemDTO);
        return "report/reportItemDetail";
    }

    // 신고 항목 목록을 조회하는 메서드
    @GetMapping({"/report/item", "/report/item/{page}"})
    public String reportNew(Model model, Principal principal,
                            @PathVariable("page") Optional<Integer> page){
        // 만약 OAuth로 등록되지 않은 회원이라면 로그인 페이지로 리디렉션
        if(memberService.findUnregisteredOAuthMember()){
            return "redirect:/oauth/login";
        }

        System.out.println("현재 페이지 =================" + page.orElse(0));

        // 페이지 번호가 없으면 0페이지를 조회
        Pageable pageable = PageRequest.of(page.map(integer -> integer - 1).orElse(0), 5);
        Page<ReportItemDTO> reportItems = reportItemService.getReportItemDTOPage(pageable);

        // 로그인한 사용자가 자신이 작성한 신고 항목을 구분할 수 있도록 설정
        /*if(principal != null){
            // 사용자가 작성한 신고 항목을 구분
            reportItems.getContent().forEach(reportItemDTO -> {
                if(principal.getName().equals(reportItemDTO.getReporterMemberEmail())){
                    reportItemDTO.setMyReport(true);
                }
            });
        }*/
        if(memberService.currentMemberEmail() != null || !memberService.currentMemberEmail().equals("anonymousUser")){
            // 사용자가 작성한 신고 항목을 구분
            reportItems.getContent().forEach(reportItemDTO -> {
                if(memberService.currentMemberEmail().equals(reportItemDTO.getReporterMemberEmail())){
                    reportItemDTO.setMyReport(true);
                }
            });
        }

        reportItems.getContent().forEach(reportItemDTO -> System.out.println("================" + reportItemDTO));

        System.out.println("총 페이지 수 : " + reportItems.getTotalPages());

        // 모델에 신고 항목 리스트와 최대 페이지 수를 추가
        model.addAttribute("reportItemList", reportItems);
        model.addAttribute("maxPage", 10); // 페이지네이션 한 블록당 출력할 페이지 수

        return "report/reportItemList";
    }

    // 신고 항목을 업데이트하는 메서드
    @PutMapping("/reportItem/update")
    public @ResponseBody ResponseEntity updateReportItem(@RequestBody @Valid ReportItemDTO reportItemDTO,
                                                         BindingResult bindingResult,
                                                         Principal principal){
        System.out.println("=========================put 진입");
        // 유효성 검사에서 오류가 발생하면 오류 메시지를 반환
        if(bindingResult.hasErrors()){
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();

            for(FieldError fieldError : fieldErrors){
                sb.append(fieldError.getDefaultMessage());
            }

            return new ResponseEntity<>(sb.toString(), HttpStatus.BAD_REQUEST);
        }
        // 사용자가 로그인되어 있지 않으면 오류 메시지를 반환
        if(principal == null) return new ResponseEntity<>("로그인이 필요합니다.", HttpStatus.BAD_REQUEST);
        String email = principal.getName();
        System.out.println(email);

        reportItemDTO.setReporterMemberEmail(email);
        System.out.println(reportItemDTO);
        Long reportItemId;

        // 신고 항목을 업데이트하고 성공하면 항목 ID를 반환, 실패하면 오류 메시지를 반환
        try {
            reportItemId = reportItemService.updateReportItem(reportItemDTO);
        } catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(reportItemId, HttpStatus.OK);
    }

    // 특정 신고 항목의 정보를 조회하는 메서드
    @GetMapping("/reportItem/{id}")
    public @ResponseBody ResponseEntity get(@PathVariable("id") Long id){
        System.out.println(id);
        ReportItemDTO reportItemDTO = reportItemService.getReportItemDTOById(id);
        return new ResponseEntity<>(reportItemDTO, HttpStatus.OK);
    }

    // 신고 항목을 삭제하는 메서드
    @DeleteMapping("/delete/reportItem/{id}")
    public @ResponseBody ResponseEntity deleteReportItem(@PathVariable("id") Long id){
        System.out.println("========================== delete :" + id);
        // 신고 항목을 삭제하고 성공하면 성공 메시지를 반환, 실패하면 오류 메시지를 반환
        try {
            reportItemService.deleteReportItem(id);
            return new ResponseEntity<>("성공적으로 삭제되었습니다.", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("삭제 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 신고 항목을 추가하는 메서드
    @PostMapping("/report/reportItem")
    public @ResponseBody ResponseEntity reportItem(@RequestBody @Valid ReportItemDTO reportItemDTO, BindingResult bindingResult){
        System.out.println("=========================post 진입");
        // 유효성 검사에서 오류가 발생하면 오류 메시지를 반환
        if(bindingResult.hasErrors()){
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();

            for(FieldError fieldError : fieldErrors){
                sb.append(fieldError.getDefaultMessage());
            }

            return new ResponseEntity<>(sb.toString(), HttpStatus.BAD_REQUEST);
        }

        // 사용자가 로그인되어 있지 않으면 오류 메시지를 반환
        if(memberService.currentMemberEmail() == null || memberService.currentMemberEmail().equals("anonymousUser"))
            return new ResponseEntity<>("로그인이 필요합니다.", HttpStatus.BAD_REQUEST);
        String email = memberService.currentMemberEmail();

        reportItemDTO.setReporterMemberEmail(email);
        Long reportItemId;

        // 신고 항목을 추가하고 성공하면 항목 ID를 반환, 실패하면 오류 메시지를 반환
        try {
            reportItemId = reportItemService.addReportItem(reportItemDTO);
        } catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(reportItemId, HttpStatus.OK);
    }

    // 신고 항목을 처리하는 메서드
    @PostMapping("/report/process")
    public @ResponseBody ResponseEntity processItem(@RequestBody ReportCommandDTO reportCommandDTO){
        // 신고 항목을 조회
        ReportItemDTO reportItemDTO = reportItemService.getReportItemDTOById(reportCommandDTO.getId());
        // 신고 항목을 처리하고 성공하면 항목 ID를 반환, 실패하면 오류 메시지를 반환
        try {
            reportItemService.processReportItem(reportItemDTO, reportCommandDTO.getCommand());
        } catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(reportCommandDTO.getId(), HttpStatus.OK);
    }
}
