package com.fashionflow.dto;

import com.fashionflow.constant.Gender;
import com.fashionflow.entity.Member;
import com.fashionflow.entity.ProfileImage;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.modelmapper.ModelMapper;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
    @Size(min = 11, max = 11, message = "전화번호는 11자리여야 합니다.")
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

    private String intro; // 사용자 소개

    private Double mannerScore; // 매너 점수

    private ProfileImageDTO profileImageDTO; // 프로필 사진

    private Long sellCount; // 판매수

    private static ModelMapper modelMapper = new ModelMapper();

    private String provider;

    private String providerId;

    // MultipartFile 필드 추가
    private MultipartFile profileImageFile;

    /* Member 엔티티를 DTO로 변환 : 비밀번호, 주소 등 민감한 정보가 같이 들어감 */
    public static MemberFormDTO entityToDTO(Member member){
        MemberFormDTO memberFormDTO = modelMapper.map(member, MemberFormDTO.class);
        if (member.getProfileImage() != null) {
            memberFormDTO.setProfileImageDTO(modelMapper.map(member.getProfileImage(), ProfileImageDTO.class));
        }
        return memberFormDTO;
    }

    /* Member 엔티티를 DTO로 변환 : 저장할 필드를 일일이 정하기 때문에 민감한 정보가 들어가지 않음 */
    public static MemberFormDTO entityToDTOSafe(Member member){
        MemberFormDTO memberFormDTO = MemberFormDTO.builder()
                .name(member.getName())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .regdate(member.getRegdate())
                .intro(member.getIntro())
                .mannerScore(member.getMannerScore())
                .build();
        if (member.getProfileImage() != null) {
            memberFormDTO.setProfileImageDTO(modelMapper.map(member.getProfileImage(), ProfileImageDTO.class));
        }
        return memberFormDTO;
    }

}
