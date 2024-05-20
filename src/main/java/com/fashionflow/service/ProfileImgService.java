package com.fashionflow.service;

import com.fashionflow.entity.ProfileImage;
import com.fashionflow.repository.ProfileImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

@Service
@RequiredArgsConstructor
@Transactional
public class ProfileImgService {

    @Value("${profileImgLocation}")
    private String profileImgLocation;

    private final ProfileImageRepository profileImageRepository;

    private final FileService fileService;

    public void saveProfileImage(ProfileImage profileImage, MultipartFile profileImageFile) throws Exception {
        String oriImgName = profileImageFile.getOriginalFilename();
        String imgName = "";
        String imgUrl = "";

        // 파일 업로드
        if (!StringUtils.isEmpty(oriImgName)) {
            imgName = fileService.uploadFile(profileImgLocation, oriImgName, profileImageFile.getBytes());
            imgUrl = "/profile/" + imgName;
        }

        // 프로필 이미지 정보 저장
        profileImage.updateProfileImage(oriImgName, imgName, imgUrl);

        // 프로필 이미지 저장 (member와의 연관 관계는 MemberService에서 설정)
        profileImageRepository.save(profileImage);
    }

    public void updateProfileImage(ProfileImage profileImage, MultipartFile profileImageFile) throws Exception {
        if (!profileImageFile.isEmpty()) {
            // 기존 이미지 파일 삭제
            if (!StringUtils.isEmpty(profileImage.getImgName())) {
                fileService.deleteFile(profileImgLocation + "/" + profileImage.getImgName());
            }

            String oriImgName = profileImageFile.getOriginalFilename();
            String imgName = fileService.uploadFile(profileImgLocation, oriImgName, profileImageFile.getBytes());
            String imgUrl = "/profile/" + imgName;
            profileImage.updateProfileImage(oriImgName, imgName, imgUrl);
            // 변경 내용 저장
            profileImageRepository.save(profileImage);
        }
    }

    // 회원 ID를 사용하여 해당 회원의 프로필 이미지를 가져오는 메서드
    public ProfileImage getProfileImageByMemberId(Long memberId) {
        return profileImageRepository.findByMemberId(memberId);
    }
}
