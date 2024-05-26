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
            if (!StringUtils.isEmpty(profileImage.getImgName())) {
                fileService.deleteFile(profileImgLocation + "/" + profileImage.getImgName());
            }

            String oriImgName = profileImageFile.getOriginalFilename();
            String imgName = fileService.uploadFile(profileImgLocation, oriImgName, profileImageFile.getBytes());
            String imgUrl = "/profile/" + imgName;
            profileImage.updateProfileImage(oriImgName, imgName, imgUrl);
            profileImageRepository.save(profileImage);
        }
    }

    public void deleteProfileImage(ProfileImage profileImage) {
        // 프로필 이미지가 존재하는지 확인
        if (profileImage != null) {
            // 프로필 이미지 삭제
            profileImageRepository.delete(profileImage);
            // 여기에서 실제 이미지 파일도 삭제하는 코드를 추가할 수 있습니다.
        }
    }
}


