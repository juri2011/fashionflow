package com.fashionflow.repository;

import com.fashionflow.entity.Member;
import com.fashionflow.entity.ProfileImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileImageRepository extends JpaRepository<ProfileImage, Long> {

    // 주어진 회원 ID에 해당하는 프로필 이미지를 찾는 메서드
    public ProfileImage findByMemberId(Long memberId);
}