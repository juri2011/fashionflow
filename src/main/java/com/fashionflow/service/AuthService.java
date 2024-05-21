package com.fashionflow.service;

import com.fashionflow.dto.ReqAuthenticateCodeDTO;
import com.fashionflow.dto.ReqSendEmailAuthenticationDTO;
import com.fashionflow.dto.ResDTO;
import com.fashionflow.dto.ResetPasswordDTO;
import com.fashionflow.entity.Member;
import com.fashionflow.entity.MemberAuthenticationCode;
import com.fashionflow.repository.MemberAuthenticationCodeRepository;
import com.fashionflow.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    // 멤버 인증 코드 리포지토리
    private final MemberAuthenticationCodeRepository memberAuthenticationCodeRepository;
    // 멤버 리포지토리
    private final MemberRepository memberRepository;
    // 비밀번호 인코더
    private final PasswordEncoder passwordEncoder;
    // 이메일 서비스
    private final EmailService emailService;

    // 이메일 인증 코드를 전송하는 메서드
    @Transactional
    public ResponseEntity<?> sendEmailAuthentication(ReqSendEmailAuthenticationDTO reqSendEmailAuthenticationDTO) {
        String email = reqSendEmailAuthenticationDTO.getEmail();
        String name = reqSendEmailAuthenticationDTO.getName();

        // 사용자 이름과 이메일 검증
        Member member = memberRepository.findByEmail(email);
        if (member == null || !member.getName().equals(name)) {
            return new ResponseEntity<>(
                    ResDTO.builder()
                            .code(-1)
                            .message("일치하는 회원 정보가 없습니다.")
                            .build(),
                    HttpStatus.BAD_REQUEST);
        }

        // 랜덤 인증 코드 생성
        String authenticationCode = createAuthenticationCode();

        // emailService로 메일 발송
        if (!emailService.sendEmailAuthentication(reqSendEmailAuthenticationDTO, authenticationCode)) {
            return new ResponseEntity<>(
                    ResDTO.builder()
                            .code(-1)
                            .message("인증 번호 발송 실패")
                            .build(),
                    HttpStatus.BAD_REQUEST);
        }

        // 이전 인증 코드 무효화
        Optional<MemberAuthenticationCode> beforeMemberAuthenticationCodeEntityOptional =
                memberAuthenticationCodeRepository.findByEmailAndEndDateAfterAndDeleteDateIsNull(email, LocalDateTime.now());

        beforeMemberAuthenticationCodeEntityOptional.ifPresent(code -> {
            code.setDeleteDate(LocalDateTime.now());
            memberAuthenticationCodeRepository.save(code);
        });

        // 새로운 인증 코드 저장
        MemberAuthenticationCode memberAuthenticationCodeEntity = MemberAuthenticationCode.builder()
                .email(email)
                .code(authenticationCode)
                .isVerified(false)
                .endDate(LocalDateTime.now().plus(5, ChronoUnit.MINUTES))
                .createDate(LocalDateTime.now())
                .build();
        memberAuthenticationCodeRepository.save(memberAuthenticationCodeEntity);

        return new ResponseEntity<>(
                ResDTO.builder()
                        .code(0)
                        .message("인증 번호 발송 성공")
                        .build(),
                HttpStatus.OK);
    }

    // 랜덤 인증번호 생성 함수
    public String createAuthenticationCode() {
        // 8자리, 문자, 숫자 포함 문자열 생성
        return RandomStringUtils.random(8, true, true);
    }

    // 이메일 인증 코드를 확인하는 메서드
    @Transactional
    public HttpEntity<?> authenticateCode(ReqAuthenticateCodeDTO reqAuthenticateCodeDTO) {

        // 유효한 인증 코드 데이터를 찾아서
        Optional<MemberAuthenticationCode> memberAuthenticationCodeEntityOptional = memberAuthenticationCodeRepository
                .findByEmailAndEndDateAfterAndDeleteDateIsNull(reqAuthenticateCodeDTO.getEmail(),
                        LocalDateTime.now());

        // 없으면 인증 코드 없음 반환
        if (memberAuthenticationCodeEntityOptional.isEmpty()) {
            return new ResponseEntity<>(
                    ResDTO.builder()
                            .code(-1)
                            .message("인증 코드 없음")
                            .build(),
                    HttpStatus.BAD_REQUEST);
        }

        // 있으면 찾아서
        MemberAuthenticationCode memberAuthenticationCodeEntity = memberAuthenticationCodeEntityOptional.get();

        // 해당 entity의 인증 코드와 입력한 인증 코드가 일치하는 지 검증
        if (memberAuthenticationCodeEntity.getCode().equals(reqAuthenticateCodeDTO.getCode())) {
            // 인증 성공 처리
            memberAuthenticationCodeEntity.setVerified(true);

            return new ResponseEntity<>(
                    ResDTO.builder()
                            .code(0)
                            .message("인증 성공")
                            .build(),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(
                    ResDTO.builder()
                            .code(-1)
                            .message("인증 실패")
                            .build(),
                    HttpStatus.BAD_REQUEST);
        }

    }

    // 비밀번호를 재설정하는 메서드
    @Transactional
    public ResponseEntity<?> resetPassword(ResetPasswordDTO resetPasswordDTO) {
        Member member = memberRepository.findByEmail(resetPasswordDTO.getEmail());
        if (member == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "계정을 찾을 수 없습니다.", "code", -1));
        }

        // 새로운 비밀번호를 인코딩하여 저장
        String encodedPassword = passwordEncoder.encode(resetPasswordDTO.getNewPassword());
        member.setPwd(encodedPassword);
        memberRepository.save(member);

        return ResponseEntity.ok(Map.of("message", "비밀번호가 성공적으로 변경되었습니다.", "code", 0));
    }
}