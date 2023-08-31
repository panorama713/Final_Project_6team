package com.example.hiddenpiece.controller.community;

import com.example.hiddenpiece.exception.CustomException;
import com.example.hiddenpiece.service.image.ArticleImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static com.example.hiddenpiece.exception.CustomExceptionCode.INTERNAL_ERROR;
import static com.example.hiddenpiece.exception.CustomExceptionCode.UNSUPPORTED_IMAGE_FORMAT;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/articles/{articleId}/images")
public class ArticleImageController {
    private final ArticleImageService articleImageService;

    private String getUsername(Authentication auth) {
        return auth.getName();
    }

    private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads";

    /**
     * GET /{imageName:.+}
     * 이미지 조회
     */
    @GetMapping("/{imageName:.+}")
    public ResponseEntity<Resource> viewImage(
            @PathVariable Long articleId, @PathVariable String imageName
    ) {
        Path imagePath = Paths.get(UPLOAD_DIR, "article_images", String.valueOf(articleId), imageName);
        Resource imageResource;

        try {
            imageResource = new UrlResource(imagePath.toUri());
        } catch (MalformedURLException e) {
            throw new CustomException(INTERNAL_ERROR);
        }

        if (!imageResource.exists() || !imageResource.isReadable()) {
            imageResource = new ClassPathResource("static/img/default.png");
            imageName = "default.jpg";
        }

        String contentType;
        if (imageName.endsWith(".jpg") || imageName.endsWith(".jpeg")) {
            contentType = "image/jpeg";
        } else if (imageName.endsWith(".png")) {
            contentType = "image/png";
        } else {
            throw new CustomException(UNSUPPORTED_IMAGE_FORMAT);
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + imageName + "\"")
                .body(imageResource);
    }

    /**
     * PUT
     * 이미지 수정 - 특정
     */
    @PutMapping
    public ResponseEntity<Void> updateSpecificImage(
            @RequestPart List<Long> imageIds,
            @RequestPart List<MultipartFile> images,
            @PathVariable Long articleId,
            Authentication auth
    ) throws IOException {
        articleImageService.updateSpecificImage(imageIds, images, getUsername(auth), articleId);
        return ResponseEntity.noContent().build();
    }

    /**
     * DELETE
     * 이미지 삭제 - 특정
     */
    @DeleteMapping
    public ResponseEntity<Void> deleteSpecificImages(
            @RequestPart List<Long> imageIds,
            @PathVariable Long articleId,
            Authentication auth
    ) {
        articleImageService.deleteSpecificImage(imageIds, getUsername(auth), articleId);
        return ResponseEntity.noContent().build();
    }
}
