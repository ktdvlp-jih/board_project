package com.rsp.platform.common.file;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.*;
import java.io.IOException;
import java.util.UUID;

@Service
public class FileStorageService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    // 실제 파일 저장 (디렉토리 생성 포함)
    public String save(MultipartFile file, String saveFileName) throws IOException {
        Path dirPath = Paths.get(uploadDir);
        if (Files.notExists(dirPath)) {
            Files.createDirectories(dirPath);
        }
        Path filePath = dirPath.resolve(saveFileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        return filePath.toString();
    }

    // 저장 파일명 생성 (UUID 등 중복 방지)
    public String generateSaveName(String originalFilename) {
        String ext = "";
        int dotIdx = originalFilename.lastIndexOf('.');
        if (dotIdx > -1) {
            ext = originalFilename.substring(dotIdx);
        }
        return UUID.randomUUID().toString() + ext;
    }
}
