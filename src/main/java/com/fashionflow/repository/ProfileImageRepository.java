package com.fashionflow.repository;

import com.fashionflow.entity.Member;
import com.fashionflow.entity.ProfileImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileImageRepository extends JpaRepository<ProfileImage, Long> {

}
