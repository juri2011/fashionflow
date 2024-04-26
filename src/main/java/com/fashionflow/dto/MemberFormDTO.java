package com.fashionflow.dto;

import com.fashionflow.constant.Gender;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MemberFormDTO {
    private String name; // 이름
    private String email; // 이메일
    private String pwd; // 비밀번호
    private String nickname; // 별명
    private String phone; // 전화번호
    private LocalDate birth; // 생년월일
    private Gender gender; // 성별
    private String userAddr; // 주소
    private String userDaddr; // 상세주소
    private String userStnum; // 지번
    private LocalDateTime regdate; // 가입일
}
