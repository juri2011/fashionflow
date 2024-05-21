package com.fashionflow.service;


import com.fashionflow.dto.ReqSendEmailAuthenticationDTO;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmailService {
    private final JavaMailSender javaMailSender; // 이메일 발송기

    private final TemplateEngine templateEngine; // 템플릿 엔진

    public Boolean sendEmailAuthentication(ReqSendEmailAuthenticationDTO reqEmailAuthenticationApiV1DTO, String authenticationCode) {
        // 메시지 객체 생성하고
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            // 이메일 제목 설정
            message.setSubject("사이트 회원가입 인증번호 입니다.");

            // 이메일 수신자 설정
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(reqEmailAuthenticationApiV1DTO.getEmail(), "", "UTF-8"));

            // 이메일 내용 설정
            message.setText(setContext(authenticationCode), "UTF-8", "html");

            // 송신
            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        }
        // 다 성공했다면
        return true;
    }

    // 생성해놓은 html에 인증 코드를 넣어서 반환
    private String setContext(String authenticationCode) {
        Context context = new Context();
        context.setVariable("authenticationCode", authenticationCode);
        return templateEngine.process("email-authentication", context);
    }
}
