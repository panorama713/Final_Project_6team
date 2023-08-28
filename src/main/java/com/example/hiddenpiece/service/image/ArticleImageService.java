package com.example.hiddenpiece.service.image;

import com.example.hiddenpiece.domain.dto.community.image.ArticleImageResponseDto;
import com.example.hiddenpiece.domain.entity.community.Article;
import com.example.hiddenpiece.domain.entity.image.ArticleImage;
import com.example.hiddenpiece.domain.repository.community.ArticleRepository;
import com.example.hiddenpiece.domain.repository.image.ArticleImageRepository;
import com.example.hiddenpiece.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.hiddenpiece.exception.CustomExceptionCode.*;

@RequiredArgsConstructor
@Slf4j
@Service
public class ArticleImageService {
    private final ArticleImageRepository articleImageRepository;
    private final ArticleImageHandler articleImageHandler;
    private final ArticleRepository articleRepository;

    /**
     * 이미지 등록
     */
    @Transactional
    public List<ArticleImageResponseDto> createArticleImage(
            List<MultipartFile> multipartFiles, String username, Long articleId
    ) throws IOException {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_ARTICLE));
        List<ArticleImage> imageList = articleImageHandler.parseFileInfo(multipartFiles, username, article);
        articleImageRepository.saveAll(imageList);
        log.info("#log# 데이터베이스 저장 - 사용자 [{}] -> 게시글 [{}] -> 이미지 {}개", username, articleId, imageList.size());
        return imageList.stream()
                .map(ArticleImageResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 이미지 조회
     */
    @Transactional(readOnly = true)
    public List<ArticleImageResponseDto> readAllArticleImages(Long articleId) {
        List<ArticleImage> imageList = articleImageRepository.findAllByArticleId(articleId);
        return imageList.stream()
                .map(ArticleImageResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 이미지 수정
     */
    @Transactional
    public void updateArticleImage(
            List<MultipartFile> updatedImages, String username, Long articleId
    ) throws IOException {
        if(updatedImages != null && !updatedImages.isEmpty()) {
            deleteArticleImage(username, articleId);
            Article article = articleRepository.findById(articleId)
                    .orElseThrow(() -> new CustomException(NOT_FOUND_ARTICLE));
            List<ArticleImage> newImageList = articleImageHandler.parseFileInfo(updatedImages, username, article);
            articleImageRepository.saveAll(newImageList);
            log.info("#log# 데이터베이스 수정 - 사용자 [{}] -> 게시글 [{}] -> 이미지 {}개", username, articleId, newImageList.size());
        }
    }

    /**
     * 이미지 삭제
     */
    @Transactional
    public void deleteArticleImage(String username, Long articleId) {
        List<ArticleImage> existingImages = articleImageRepository.findAllByArticleId(articleId);
        for(ArticleImage image : existingImages) {
            deletePhysicalImage(image.getImageUrl());
        }
        articleImageRepository.deleteAll(existingImages);
        log.info("#log# 데이터베이스 소프트 삭제 - 사용자 [{}] -> 게시글 [{}] -> 이미지 {}개", username, articleId, existingImages.size());
    }

    private void deletePhysicalImage(String imageUrl) {
        File file = new File(imageUrl);
        if(file.exists()) {
            if(!file.delete()) {
                log.error("#log# 이미지 삭제 실패 [{}]", imageUrl);
            }
        }
    }

    /**
     * 이미지 수정 - 특정
     */
    @Transactional
    public void updateSpecificImage(
            List<Long> imageIds, List<MultipartFile> updatedImages, String username, Long articleId
    ) throws IOException {
        if (imageIds.size() != updatedImages.size()) {
            throw new CustomException(IMAGE_COUNT_MISMATCH);
        }
        for (int i = 0; i < imageIds.size(); i++) {
            ArticleImage existingImage = articleImageRepository.findById(imageIds.get(i))
                    .orElseThrow(() -> new CustomException(NOT_FOUND_IMAGE));
            Article connectedArticle = existingImage.getArticle();
            if (connectedArticle == null || !connectedArticle.getId().equals(articleId)) {
                throw new CustomException(NOT_FOUND_ARTICLE);
            }
            if (!connectedArticle.getUser().getUsername().equals(username)) {
                throw new CustomException(NOT_MATCH_WRITER);
            }
            deletePhysicalImage(existingImage.getImageUrl());
            List<MultipartFile> singleUpdatedImageList = Collections.singletonList(updatedImages.get(i));
            ArticleImage updatedImage = articleImageHandler.parseFileInfo(singleUpdatedImageList, username, connectedArticle).get(0);
            existingImage.updateArticleImage(updatedImage);
            articleImageRepository.save(existingImage);
            log.info("#log# 데이터베이스 수정 - 사용자 [{}] -> 게시글 [{}] -> 이미지 [{}]", username, articleId, imageIds.get(i));
        }
    }

    /**
     * 이미지 삭제 - 특정
     */
    @Transactional
    public void deleteSpecificImage(List<Long> imageIds, String username, Long articleId) {
        for (Long imageId : imageIds) {
            ArticleImage existingImage = articleImageRepository.findById(imageId)
                    .orElseThrow(() -> new CustomException(NOT_FOUND_IMAGE));
            Article connectedArticle = existingImage.getArticle();
            if (connectedArticle == null || !connectedArticle.getId().equals(articleId)) {
                throw new CustomException(NOT_FOUND_ARTICLE);
            }
            if (!connectedArticle.getUser().getUsername().equals(username)) {
                throw new CustomException(NOT_MATCH_WRITER);
            }
            deletePhysicalImage(existingImage.getImageUrl());
            articleImageRepository.deleteById(imageId);
            log.info("#log# 데이터베이스 소프트 삭제 - 사용자 [{}] -> 게시글 [{}] -> 이미지 [{}]", username, articleId, imageId);
        }
    }
}
