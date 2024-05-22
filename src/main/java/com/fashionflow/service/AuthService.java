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
import org.springframework.scheduling.annotation.Scheduled;
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

    private final MemberAuthenticationCodeRepository memberAuthenticationCodeRepository;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Transactional
    public ResponseEntity<?> sendEmailAuthentication(ReqSendEmailAuthenticationDTO reqSendEmailAuthenticationDTO) {
        String email = reqSendEmailAuthenticationDTO.getEmail();
        String name = reqSendEmailAuthenticationDTO.getName();

        Member member = memberRepository.findByEmail(email);
        if (member == null || !member.getName().equals(name)) {
            return new ResponseEntity<>(
                    ResDTO.builder()
                            .code(-1)
                            .message("일치하는 회원 정보가 없습니다.")
                            .build(),
                    HttpStatus.BAD_REQUEST);
        }

        String authenticationCode = createAuthenticationCode();

        if (!emailService.sendEmailAuthentication(reqSendEmailAuthenticationDTO, authenticationCode)) {
            return new ResponseEntity<>(
                    ResDTO.builder()
                            .code(-1)
                            .message("인증 번호 발송 실패")
                            .build(),
                    HttpStatus.BAD_REQUEST);
        }

        Optional<MemberAuthenticationCode> beforeMemberAuthenticationCodeEntityOptional =
                memberAuthenticationCodeRepository.findByEmailAndEndDateAfterAndDeleteDateIsNull(email, LocalDateTime.now());

        beforeMemberAuthenticationCodeEntityOptional.ifPresent(code -> {
            code.setDeleteDate(LocalDateTime.now());
            memberAuthenticationCodeRepository.save(code);
        });

        MemberAuthenticationCode memberAuthenticationCodeEntity = MemberAuthenticationCode.builder()
                .email(email)
                .code(authenticationCode)
                .isVerified(false)
                .endDate(LocalDateTime.now().plus(3, ChronoUnit.MINUTES))  // 3분으로 설정
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

    public String createAuthenticationCode() {
        return RandomStringUtils.random(8, true, true);
    }

    @Transactional
    public HttpEntity<?> authenticateCode(ReqAuthenticateCodeDTO reqAuthenticateCodeDTO) {
        Optional<MemberAuthenticationCode> memberAuthenticationCodeEntityOptional = memberAuthenticationCodeRepository
                .findByEmailAndEndDateAfterAndDeleteDateIsNull(reqAuthenticateCodeDTO.getEmail(),
                        LocalDateTime.now());

        if (memberAuthenticationCodeEntityOptional.isEmpty()) {
            return new ResponseEntity<>(
                    ResDTO.builder()
                            .code(-1)
                            .message("인증 코드 없음")
                            .build(),
                    HttpStatus.BAD_REQUEST);
        }

        MemberAuthenticationCode memberAuthenticationCodeEntity = memberAuthenticationCodeEntityOptional.get();

        if (memberAuthenticationCodeEntity.getCode().equals(reqAuthenticateCodeDTO.getCode())) {
            memberAuthenticationCodeEntity.setVerified(true);
            memberAuthenticationCodeEntity.setUpdateDate(LocalDateTime.now());
            memberAuthenticationCodeRepository.save(memberAuthenticationCodeEntity);

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

    @Transactional
    public ResponseEntity<?> resetPassword(ResetPasswordDTO resetPasswordDTO) {
        Member member = memberRepository.findByEmail(resetPasswordDTO.getEmail());
        if (member == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "계정을 찾을 수 없습니다.", "code", -1));
        }

        String encodedPassword = passwordEncoder.encode(resetPasswordDTO.getNewPassword());
        member.setPwd(encodedPassword);
        memberRepository.save(member);

        return ResponseEntity.ok(Map.of("message", "비밀번호가 성공적으로 변경되었습니다.", "code", 0));
    }

    // 스케줄링 작업: 매 1분마다 만료된 인증번호를 삭제합니다.
    @Scheduled(cron = "0 */1 * * * *")
    @Transactional
    public void deleteExpiredCodes() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        memberAuthenticationCodeRepository.deleteExpiredCodes(currentDateTime);
    }
}
