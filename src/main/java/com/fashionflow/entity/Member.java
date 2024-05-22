package com.fashionflow.entity;

import com.fashionflow.constant.Gender;
import com.fashionflow.constant.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "profileImage")
public class Member implements UserDetails {

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

    @Column(nullable = false, columnDefinition = "Double default 5.0") //DB 스키마 적용 기본값
    @Builder.Default //JPA 적용 기본값
    private Double mannerScore = 5.0; //매너점수

    @Column(nullable = false)
    private String userStnum; //지번

    @Column(nullable = false)
    private String userAddr; //주소

    @Column(nullable = false)
    private String userDaddr; //상세주소

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id")
    private ProfileImage profileImage;

    @Column(nullable = false)
    private LocalDateTime regdate; //가입일

    @Column(columnDefinition = "longtext")
    private String intro; //사용자 소개

    //시큐리티 - 사용자 등급
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Role role = Role.USER; // 기본값 USER

    // provider : google이 들어감
    @Column(name = "provider")
    private String provider;

    // providerId : 구굴 로그인 한 유저의 고유 ID가 들어감
    @Column(name = "provider_id")
    private String providerId;

    // 매너점수 업데이트
    public void updateMannerScore(Double newScore) {

        this.mannerScore = Math.round(newScore * 10) / 10.0;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.role.name()));
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.pwd;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
