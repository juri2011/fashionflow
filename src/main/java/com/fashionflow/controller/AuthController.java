package com.fashionflow.controller;

import com.fashionflow.dto.ReqAuthenticateCodeDTO;
import com.fashionflow.dto.ReqSendEmailAuthenticationDTO;
import com.fashionflow.dto.ResetPasswordDTO;
import com.fashionflow.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    // 이메일 인증 번호 요청
    @PostMapping("/email-authentication")
    public ResponseEntity<?> sendEmailAuthentication(
            @RequestBody ReqSendEmailAuthenticationDTO reqSendEmailAuthenticationDTO) {
        return authService.sendEmailAuthentication(reqSendEmailAuthenticationDTO);
    }

    // 인증 번호 검증
    @PostMapping("/authentication-code")
    public HttpEntity<?> authenticateCode(@RequestBody ReqAuthenticateCodeDTO reqSendEmailAuthenticationDTO) {
        return authService.authenticateCode(reqSendEmailAuthenticationDTO);
    }


    // 비밀번호 재설정
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordDTO resetPasswordDTO) {
        return authService.resetPassword(resetPasswordDTO);
    }



}
