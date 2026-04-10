package com.ohgiraffers.COZYbe.domain.files.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

@Service
public class S3Service {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${app.s3.presign-exp-minutes:60}")
    private long presignExpMinutes;

    public S3Service(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    public String upload(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("업로드할 파일이 없습니다.");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("이미지 파일만 업로드할 수 있습니다.");
        }

        String originalName = file.getOriginalFilename();
        String safeName = (originalName == null || originalName.isBlank())
                ? "file"
                : originalName.replaceAll("\\s+", "_");
        String fileName = "profile_images/" + UUID.randomUUID() + "-" + safeName;

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(contentType);
        metadata.setContentLength(file.getSize());

        PutObjectRequest request = new PutObjectRequest(bucket, fileName, file.getInputStream(), metadata);

        amazonS3.putObject(request);

        return fileName;
    }

    public String getPresignedUrl(String keyOrUrl) {
        if (keyOrUrl == null || keyOrUrl.isBlank()) {
            return null;
        }
        if (keyOrUrl.startsWith("http://") || keyOrUrl.startsWith("https://")) {
            return keyOrUrl;
        }

        long expMillis = System.currentTimeMillis() + (presignExpMinutes * 60 * 1000);
        Date expiration = new Date(expMillis);
        return amazonS3.generatePresignedUrl(bucket, keyOrUrl, expiration).toString();
    }
}
