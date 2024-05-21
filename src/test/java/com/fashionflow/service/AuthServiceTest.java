package com.fashionflow.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.fashionflow.dto.ReqSendEmailAuthenticationDTO;
import com.fashionflow.dto.ResDTO;
import com.fashionflow.entity.Member;
import com.fashionflow.repository.MemberAuthenticationCodeRepository;
import com.fashionflow.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class AuthServiceTest {

    // MemberRepository의 Mock 객체
    @Mock
    private MemberRepository memberRepository;

    // MemberAuthenticationCodeRepository의 Mock 객체
    @Mock
    private MemberAuthenticationCodeRepository memberAuthenticationCodeRepository;

    // EmailService의 Mock 객체
    @Mock
    private EmailService emailService;

    // AuthService에 Mock 객체를 주입하는데 사용되는 InjectMocks 어노테이션
    @InjectMocks
    private AuthService authService;

    // 올바르지 않은 이메일 또는 이름 테스트
    @Test
    public void testSendEmailAuthentication_InvalidEmailOrName() {
        // Given
        // 잘못된 이메일과 이름을 가진 DTO 객체 생성
        ReqSendEmailAuthenticationDTO request = new ReqSendEmailAuthenticationDTO();
        request.setEmail("invalid@example.com");
        request.setName("Invalid Name");

        // MemberRepository의 findByEmail 메서드가 null을 반환하도록 Mock 설정
        when(memberRepository.findByEmail(anyString())).thenReturn(null);

        // When
        // AuthService의 sendEmailAuthentication 메서드 호출
        ResponseEntity<?> response = authService.sendEmailAuthentication(request);

        // Then
        // 반환된 ResponseEntity가 올바른지 확인
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(-1, ((ResDTO) response.getBody()).getCode());
        assertEquals("일치하는 회원 정보가 없습니다.", ((ResDTO) response.getBody()).getMessage());
    }

    // 올바른 이메일과 이름 테스트
    @Test
    public void testSendEmailAuthentication_ValidEmailAndName() {
        // Given
        // 올바른 이메일과 이름을 가진 DTO 객체 생성
        ReqSendEmailAuthenticationDTO request = new ReqSendEmailAuthenticationDTO();
        request.setEmail("valid@example.com");
        request.setName("Valid Name");

        // 올바른 이메일과 이름을 가진 Member 객체 생성
        Member validMember = new Member();
        validMember.setEmail("valid@example.com");
        validMember.setName("Valid Name");

        // MemberRepository의 findByEmail 메서드가 validMember를 반환하도록 Mock 설정
        when(memberRepository.findByEmail(anyString())).thenReturn(validMember);
        // emailService의 sendEmailAuthentication 메서드가 true를 반환하도록 Mock 설정
        when(emailService.sendEmailAuthentication(any(ReqSendEmailAuthenticationDTO.class), anyString())).thenReturn(true);

        // When
        // AuthService의 sendEmailAuthentication 메서드 호출
        ResponseEntity<?> response = authService.sendEmailAuthentication(request);

        // Then
        // 반환된 ResponseEntity가 올바른지 확인
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(0, ((ResDTO) response.getBody()).getCode());
        assertEquals("인증 번호 발송 성공", ((ResDTO) response.getBody()).getMessage());
    }

}
