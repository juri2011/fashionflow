package com.fashionflow.dto;

import com.fashionflow.constant.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MemberFormDTO {
    @NotBlank(message = "이름을 입력해주세요.")
    private String name; // 이름

    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "이메일 형식으로 입력해주세요.")
    private String email; // 이메일

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Length(min=6, max=20, message = "비밀번호는 6자리 이상 20자리 이하로 입력하세요.")
    private String pwd; // 비밀번호

    @NotBlank(message = "비밀번호를 재입력해주세요.")
    private String confirmPwd;

    @NotBlank(message = "별명을 입력해주세요.")
    private String nickname; // 별명

    @NotBlank(message = "전화번호를 입력해주세요.")
    private String phone; // 전화번호

    @NotNull(message = "생년월일을 입력해주세요.")
    private LocalDate birth; // 생년월일

    private Gender gender; // 성별

    @NotBlank(message = "주소를 입력해주세요.")
    private String userAddr; // 주소

    @NotBlank(message = "상세주소를 입력해주세요.")
    private String userDaddr; // 상세주소

    @NotBlank(message = "지번을 입력해주세요.")
    private String userStnum; // 지번

    private LocalDateTime regdate; // 가입일
}
