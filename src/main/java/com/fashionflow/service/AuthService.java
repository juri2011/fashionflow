package com.fashionflow.service;

import com.fashionflow.dto.ReqAuthenticateCodeDTO;
import com.fashionflow.dto.ReqSendEmailAuthenticationDTO;
import com.fashionflow.dto.ResDTO;
import com.fashionflow.entity.MemberAuthenticationCode;
import com.fashionflow.repository.MemberAuthenticationCodeRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final MemberAuthenticationCodeRepository memberAuthenticationCodeRepository;

    private final EmailService emailService;

    @Transactional
    public HttpEntity<?> sendEmailAuthentication(
            ReqSendEmailAuthenticationDTO reqSendEmailAuthenticationDTO) {

        // 랜덤 인증 코드 생성해서
        String authenticationCode = createAuthenticationCode();

        // emailService의 sendEmailAuthenticationCode함수로 메일을 발송하고, 성공 여부에 따라 true / false 반환
        if (!emailService.sendEmailAuthentication(reqSendEmailAuthenticationDTO, authenticationCode)) {
            // 메일 발송 실패 시 BAD_REQUEST 반환
            return new ResponseEntity<>(
                    ResDTO.builder()
                            .code(-1)
                            .message("인증 번호 발송 실패")
                            .build(),
                    HttpStatus.BAD_REQUEST);
        }

        // 메일 발송 성공 시
        // 아직 유효한 인증 코드 데이터를 찾아서
        Optional<MemberAuthenticationCode> beforeMemberAuthenticationCodeEntityOptional = memberAuthenticationCodeRepository
                .findByEmailAndEndDateAfterAndDeleteDateIsNull(
                        reqSendEmailAuthenticationDTO.getEmail(),
                        LocalDateTime.now());

        // 있으면 무효화 (delete_date 설정)
        if (beforeMemberAuthenticationCodeEntityOptional.isPresent()) {
            MemberAuthenticationCode beforeMemberAuthenticationCodeEntity = beforeMemberAuthenticationCodeEntityOptional
                    .get();
            beforeMemberAuthenticationCodeEntity.setDeleteDate(LocalDateTime.now());
            memberAuthenticationCodeRepository.save(beforeMemberAuthenticationCodeEntity);
        }

        // 인증 코드 데이터를 저장하기 위해 새 엔티티를 작성하여
        MemberAuthenticationCode memberAuthenticationCodeEntity = MemberAuthenticationCode
                .builder()
                .email(reqSendEmailAuthenticationDTO.getEmail())
                .code(authenticationCode)
                .isVerified(false)
                .endDate(LocalDateTime.now().plus(5, ChronoUnit.MINUTES))
                .createDate(LocalDateTime.now())
                .build();

        // 저장
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
}
