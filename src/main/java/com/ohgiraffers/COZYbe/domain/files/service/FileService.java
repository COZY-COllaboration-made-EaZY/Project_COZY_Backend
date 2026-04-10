package com.ohgiraffers.COZYbe.domain.files.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;

@Service
public class FileService {

    private static final String FALLBACK_DEFAULT_IMAGE_KEY = "profile_images/Default_Profile.png";

    private final S3Service s3Service;

    @Value("${app.profile.default-image-key:" + FALLBACK_DEFAULT_IMAGE_KEY + "}")
    private String defaultProfileImageKey;

    public FileService(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    public String getDefaultProfileImageDir(){
        return defaultProfileImageKey;
    }

    public String getProfileImageUrl(String keyOrUrl) {
        if (keyOrUrl == null || keyOrUrl.isBlank()) {
            return null;
        }
        return s3Service.getPresignedUrl(keyOrUrl);
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


    // 프로필 이미지 저장 (S3 업로드)
    public String saveProfileImage(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }
        return s3Service.upload(file);
    }
}
