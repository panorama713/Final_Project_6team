package com.example.hiddenpiece.service.image;

import com.example.hiddenpiece.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import static com.example.hiddenpiece.exception.CustomExceptionCode.INTERNAL_ERROR;

@Slf4j
@Component
public class UserImageHandler {
    public String parseFileInfo(Long userId, MultipartFile image) {
        if (image == null) return null;

        String profileImgDir = String.format("uploads/profile_images/%d/", userId);
        try {
            Files.createDirectories(Path.of(profileImgDir));
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new CustomException(INTERNAL_ERROR);
        }

        String originalFilename = image.getOriginalFilename();
        String[] fileNameSplit = originalFilename.split("\\.");
        String extension = fileNameSplit[fileNameSplit.length - 1];
        String profileFilename = UUID.randomUUID() + "." + extension;
        String profileImagePath = profileImgDir + profileFilename;

        try {
            image.transferTo(Path.of(profileImagePath));
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new CustomException(INTERNAL_ERROR);
        }

        return String.format("/static/profile_images/%d/%s", userId, profileFilename);
    }
}
