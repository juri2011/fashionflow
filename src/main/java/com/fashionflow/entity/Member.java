package com.fashionflow.entity;

import com.fashionflow.constant.Gender;
import com.fashionflow.constant.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {

    @Id
    @Column(name="member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //일련번호

    @Column(nullable = false, length=100)
    private String name; //이름

    @Column(nullable = false, unique = true)
    private String email; //이메일(아이디)

    @Column(nullable = false)
    private String pwd; //비밀번호

    @Column(nullable = false, length=100, unique = true)
    private String nickname; //별명

    @Column(nullable = false, unique = true, length = 100)
    private String phone; //전화번호

    @Column(nullable = false)
    private LocalDate birth; //생일
    
    //enum
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender; //성별(m: 남자, f: 여자, secret: 비공개)

    @Column(nullable = false, columnDefinition = "int default 50")
    private int mannerScore; //매너점수

    @Column(nullable = false)
    private String userStnum; //지번

    @Column(nullable = false)
    private String userAddr; //주소

    @Column(nullable = false)
    private String userDaddr; //상세주소

    @Column(nullable = false)
    private LocalDateTime regdate; //가입일

    //시큐리티 - 사용자 등급
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Role role = Role.USER; // 기본값 USER


}
