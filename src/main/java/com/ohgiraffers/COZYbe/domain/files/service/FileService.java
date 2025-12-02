package com.ohgiraffers.COZYbe.domain.files.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileService {

    private static final String UPLOAD_DIR = "uploads/profile_images";
    private static final String DEFAULT_IMAGE_DIR = UPLOAD_DIR + "Default_Profile.png";

    public String getDefaultProfileImageDir(){
        return DEFAULT_IMAGE_DIR;
    }

//    public String saveProfileImage(MultipartFile file) throws IOException {
//        if (file == null || file.isEmpty()) {
//            return null;
//        }
//
//        String originalFilename = file.getOriginalFilename();
//        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
//        String uniqueFilename = UUID.randomUUID().toString() + extension;
//
//        File uploadDir = new File(UPLOAD_DIR);
//        if (!uploadDir.exists()) {
//            uploadDir.mkdirs();
//        }
//
//        File destFile = new File(uploadDir, uniqueFilename);
//        file.transferTo(destFile);
//        return "/uploads/profile_images/" + uniqueFilename;
//    }


    // 프로필 이미지 저장
    private String saveProfileImage(MultipartFile file) throws IOException {
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null) {
            throw new IllegalArgumentException("파일 이름이 존재하지 않습니다.");
        }

        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String newFileName = UUID.randomUUID() + fileExtension;

        Path filePath = Path.of(UPLOAD_DIR, newFileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return UPLOAD_DIR + newFileName;
    }
}
