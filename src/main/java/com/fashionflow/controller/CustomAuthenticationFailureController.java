package com.fashionflow.controller;

import com.fashionflow.constant.Role;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import java.io.IOException;

import static com.fashionflow.entity.QMember.member;


public class CustomAuthenticationFailureController extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        String errorMessage = "아이디 또는 비밀번호를 확인해주세요.";

        Throwable cause = exception.getCause();
        if (cause instanceof DisabledException) {
            errorMessage = "이 계정은 접근이 차단되었습니다.";
        }

        request.getSession().setAttribute("loginErrorMsg", errorMessage);
        setDefaultFailureUrl("/members/login/error");
        super.onAuthenticationFailure(request, response, exception);
    }
}
