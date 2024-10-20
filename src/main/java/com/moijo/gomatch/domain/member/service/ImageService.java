package com.moijo.gomatch.domain.member.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class ImageService {

    @Value("${upload.path}")
    private String uploadPath;

    public String saveProfileImage(MultipartFile file, String oldImageUrl) throws IOException {
        // 기존 이미지 삭제
        if (oldImageUrl != null && !oldImageUrl.isEmpty()) {
            deleteOldImage(oldImageUrl);
        }

        // 새 이미지 저장
        String fileName = UUID.randomUUID().toString() + "_" + StringUtils.cleanPath(file.getOriginalFilename());
        Path uploadDir = Paths.get(uploadPath);

        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }

        Path filePath = uploadDir.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return "/img/member-img/" + fileName;
    }

    private void deleteOldImage(String oldImageUrl) {
        if (oldImageUrl != null && !oldImageUrl.isEmpty()) {
            try {
                String oldFileName = oldImageUrl.substring(oldImageUrl.lastIndexOf("/") + 1);
                Path oldFilePath = Paths.get(uploadPath, oldFileName);
                Files.deleteIfExists(oldFilePath);
            } catch (IOException e) {
                // 로그 처리
            }
        }
    }
}
