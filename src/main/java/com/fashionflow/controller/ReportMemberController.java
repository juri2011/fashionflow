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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

// 신고 컨트롤러
@Controller
@RequiredArgsConstructor
public class ReportMemberController {

    private final ReportMemberService reportMemberService;
    private final MemberService memberService;

    // 특정 신고 항목의 상세 정보를 조회하는 메서드
    @GetMapping("/report/memberdetail/{id}")
    public String reportMemberDetail(@AuthenticationPrincipal User user, Model model, @PathVariable("id") Long id) {
        // 만약 OAuth로 등록되지 않은 회원이라면 로그인 페이지로 리디렉션
        if (memberService.findUnregisteredOAuthMember()) {
            return "redirect:/oauth/login";
        }

        // 비회원인 경우 접근 거부 페이지로 리디렉션
        if (user == null || user.getUsername().equals("anonymousUser")) {
            return "error/accessError";
        }

        // 현재 로그인된 사용자의 권한 가져오기
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        boolean hasAdminRole = authorities.stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

        // ADMIN 역할이 없는 경우 접근 거부 페이지로 리디렉션
        if (!hasAdminRole) {
            return "error/accessError";
        }

        // 신고 항목 ID를 기반으로 신고 항목 DTO를 조회
        ReportMemberDTO reportMemberDTO = reportMemberService.getReportMemberDTOById(id);
        System.out.println(reportMemberDTO);

        // 모델에 신고 항목 DTO를 추가
        model.addAttribute("reportMember", reportMemberDTO);
        return "report/reportMemberDetail";
    }



    // 신고 항목을 추가하는 메서드
    @PostMapping("/report/reportMember")
    public @ResponseBody ResponseEntity reportMember(@RequestBody @Valid ReportMemberDTO reportMemberDTO,
                                                     BindingResult bindingResult){

        System.out.println(reportMemberDTO);

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
        System.out.println(email);

        reportMemberDTO.setReporterMemberEmail(email);
        System.out.println(reportMemberDTO);
        Long reportItemId;

        // 신고 항목을 추가하고 성공하면 항목 ID를 반환, 실패하면 오류 메시지를 반환
        try {
            reportItemId = reportMemberService.addReportMember(reportMemberDTO);
        } catch(EntityNotFoundException e) {
            // 로그인 정보가 없는 상태에서 DB에 저장하려 할 때 에러 메시지 출력
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(reportItemId, HttpStatus.OK);
    }

    // 신고 항목 목록을 조회하는 메서드
    @GetMapping({"/report/member", "/report/member/{page}"})
    public String reportNew(Model model, Principal principal,
                            @PathVariable("page") Optional<Integer> page){
        // 만약 OAuth로 등록되지 않은 회원이라면 로그인 페이지로 리디렉션
        if(memberService.findUnregisteredOAuthMember()){
            return "redirect:/oauth/login";
        }

        System.out.println("현재 페이지 =================" + page.orElse(0));

        // 페이지 번호가 없으면 0페이지를 조회
        Pageable pageable = PageRequest.of(page.map(integer -> integer - 1).orElse(0), 5);
        Page<ReportMemberDTO> reportMembers = reportMemberService.getReportMemberDTOPage(pageable);

        // 로그인한 사용자가 자신이 작성한 신고 항목을 구분할 수 있도록 설정
        if(memberService.currentMemberEmail() != null || !memberService.currentMemberEmail().equals("anonymousUser")){
            // 사용자가 작성한 신고 항목을 구분
            reportMembers.getContent().forEach(reportMemberDTO -> {
                if(memberService.currentMemberEmail().equals(reportMemberDTO.getReporterMemberEmail())){
                    reportMemberDTO.setMyReport(true);
                }
            });
        }
        reportMembers.getContent().forEach(reportMemberDTO -> System.out.println("================" + reportMemberDTO));

        System.out.println("총 페이지 수 : " + reportMembers.getTotalPages());

        // 모델에 신고 항목 리스트와 최대 페이지 수를 추가
        model.addAttribute("reportMemberList", reportMembers);
        model.addAttribute("maxPage", 10); // 페이지네이션 한 블록당 출력할 페이지 수

        return "report/reportMemberList";
    }

    // 사용자 신고 정보를 가져오는 메서드
    @GetMapping("/reportMember/{id}")
    public @ResponseBody ResponseEntity get(@PathVariable("id") Long id){
        System.out.println(id);
        // 신고 항목 ID를 기반으로 신고 항목 DTO를 조회
        ReportMemberDTO reportMemberDTO = reportMemberService.getReportMemberDTOById(id);
        return new ResponseEntity<>(reportMemberDTO, HttpStatus.OK);
    }

    // 신고 항목을 삭제하는 메서드
    @DeleteMapping("/delete/reportMember/{id}")
    public @ResponseBody ResponseEntity deleteReportMember(@PathVariable("id") Long id){
        System.out.println("========================== delete :" + id);
        // 신고 항목을 삭제하고 성공하면 성공 메시지를 반환, 실패하면 오류 메시지를 반환
        try {
            reportMemberService.deleteReportMember(id);
            return new ResponseEntity<>("성공적으로 삭제되었습니다.", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("삭제 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 신고 항목을 업데이트하는 메서드
    @PutMapping("/reportMember/update")
    public @ResponseBody ResponseEntity updateReportItem(@RequestBody @Valid ReportMemberDTO reportMemberDTO,
                                                         BindingResult bindingResult,
                                                         Principal principal){
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
        System.out.println(email);

        reportMemberDTO.setReporterMemberEmail(email);
        System.out.println(reportMemberDTO);
        Long reportMemberId;

        // 신고 항목을 업데이트하고 성공하면 항목 ID를 반환, 실패하면 오류 메시지를 반환
        try {
            reportMemberId = reportMemberService.updateReportMember(reportMemberDTO);
        } catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(reportMemberId, HttpStatus.OK);
    }

    // 신고 항목을 처리하는 메서드
    @PostMapping("/report/processMember")
    public @ResponseBody ResponseEntity processMember(@RequestBody ReportCommandDTO reportCommandDTO){
        // 신고 항목을 조회
        ReportMemberDTO reportMemberDTO = reportMemberService.getReportMemberDTOById(reportCommandDTO.getId());
        // 신고 항목을 처리하고 성공하면 항목 ID를 반환, 실패하면 오류 메시지를 반환
        try {
            reportMemberService.processReportMember(reportMemberDTO, reportCommandDTO.getCommand());
        } catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(reportCommandDTO.getId(), HttpStatus.OK);
    }
}
