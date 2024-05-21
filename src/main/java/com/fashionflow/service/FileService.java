package com.fashionflow.service;

import lombok.extern.java.Log;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

@Service
@Log
public class FileService {

    public String uploadFile(String uploadPath, String originalFileName, byte[] fileData) throws Exception {
        UUID uuid = UUID.randomUUID(); // UUID 생성
        String extension = originalFileName.substring(originalFileName.lastIndexOf(".")); // 확장자 추출
        String savedFileName = uuid.toString() + extension; // 저장 파일명 생성
        String fileUploadFullUrl = uploadPath + "/" + savedFileName; // 파일 전체 경로 생성
        FileOutputStream fos = new FileOutputStream(fileUploadFullUrl); // 파일 출력 스트림 생성
        fos.write(fileData); // 파일 데이터 쓰기
        fos.close(); // 스트림 닫기
        return savedFileName; // 저장된 파일명 반환
    }

    public void deleteFile(String filePath) throws Exception {
        File deleteFile = new File(filePath); // 삭제할 파일 객체 생성
        if (deleteFile.exists()) {
            deleteFile.delete(); // 파일 삭제
            log.info("파일을 삭제하였습니다."); // 삭제 로그
        } else {
            log.info("파일이 존재하지 않습니다."); // 파일 없음 로그
        }
    }

}