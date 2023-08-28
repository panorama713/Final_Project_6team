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
import java.util.List;
import java.util.stream.Collectors;

import static com.example.hiddenpiece.exception.CustomExceptionCode.NOT_FOUND_ARTICLE;

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
}
