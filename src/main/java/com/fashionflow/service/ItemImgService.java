package com.fashionflow.service;
import com.fashionflow.entity.ItemImg;
import com.fashionflow.repository.ItemImgRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemImgService {

    @Value("${itemImgLocation}")
    private String itemImgLocation; // 이미지 저장 위치

    private final ItemImgRepository itemImgRepository; // 이미지 저장소

    private final FileService fileService; // 파일 서비스

    public void saveItemImg(ItemImg itemImg, MultipartFile itemImgFile) throws Exception {
        String oriImgName = itemImgFile.getOriginalFilename(); // 원본 이미지 파일명
        String imgName = ""; // 저장된 이미지 파일명
        String imgUrl = ""; // 이미지 URL

        // 파일 업로드
        if (!StringUtils.isEmpty(oriImgName)) {
            imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes()); // 파일 업로드 및 파일명 반환
            imgUrl = "/images/item/" + imgName; // 이미지 URL 생성
        }

        // 상품 이미지 정보 저장
        itemImg.updateItemImg(oriImgName, imgName, imgUrl); // 이미지 정보 업데이트
        itemImgRepository.save(itemImg); // 이미지 정보 저장
    }

    public void updateItemImg(Long itemImgId, MultipartFile itemImgFile) throws Exception {
        if (!itemImgFile.isEmpty()) {
            ItemImg savedItemImg = itemImgRepository.findById(itemImgId)
                    .orElseThrow(EntityNotFoundException::new); // 기존 이미지 조회

            // 기존 이미지 파일 삭제
            if (!StringUtils.isEmpty(savedItemImg.getImgName())) {
                fileService.deleteFile(itemImgLocation + "/" + savedItemImg.getImgName()); // 기존 파일 삭제
            }

            String oriImgName = itemImgFile.getOriginalFilename(); // 새 원본 이미지 파일명
            String imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes()); // 새 파일 업로드 및 파일명 반환
            String imgUrl = "/images/item/" + imgName; // 새 이미지 URL 생성
            savedItemImg.updateItemImg(oriImgName, imgName, imgUrl); // 이미지 정보 업데이트
        }
    }


}