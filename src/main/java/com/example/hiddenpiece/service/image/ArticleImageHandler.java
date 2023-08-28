package com.example.hiddenpiece.service.image;

import com.example.hiddenpiece.domain.dto.community.image.ArticleImageRequestDto;
import com.example.hiddenpiece.domain.entity.community.Article;
import com.example.hiddenpiece.domain.entity.image.ArticleImage;
import com.example.hiddenpiece.exception.CustomException;
import com.nimbusds.oauth2.sdk.util.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.example.hiddenpiece.exception.CustomExceptionCode.UNSUPPORTED_IMAGE_FORMAT;

@Slf4j
@Component
public class ArticleImageHandler {
//    // MEMO application.yaml 설정 시 아래 주석 해제 (불필요 시 삭제 예정)
//    @Value("${image.upload.dir}")
//    private String uploadDir;
    private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads";

    /**
     * MultipartFile 기반으로 ArticleImage 객체들의 목록을 반환
     * @param multipartFiles 이미지 파일 목록
     * @param username       사용자의 아이디
     * @param article        연관된 Article 객체
     * @return               ArticleImage 객체의 목록
     * @throws IOException   파일 처리 관련 예외 발생 시
     */
    public List<ArticleImage> parseFileInfo(
            List<MultipartFile> multipartFiles, String username, Article article
    ) throws IOException {
        List<ArticleImage> imageList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(multipartFiles)) {
            String path = createDirectory(article);
            for (MultipartFile multipartFile : multipartFiles) {
                String originalFileExtension = checkImageFormat(multipartFile);
                UUID uuid = UUID.randomUUID();
                String newFileName = uuid + originalFileExtension;
                ArticleImage articleImage = buildArticleImage(multipartFile, path, newFileName, article);
                imageList.add(articleImage);
                saveFile(multipartFile, path, newFileName);
            }
            log.info("#log# 파일 처리 - 사용자 [{}] -> 게시글 [{}] -> 이미지 {}개", username, article.getId(), multipartFiles.size());
        }
        return imageList;
    }

    /**
     * 디렉토리 생성
     */
    private String createDirectory(Article article) throws IOException {
        String path = UPLOAD_DIR + "/article_images/" + article.getId();
        File file = new File(path);
        if (!file.exists()) {
            boolean wasSuccessful = file.mkdirs();
            if (!wasSuccessful) {
                log.error("#log# 디렉토리 생성 실패");
                throw new IOException("경로: " + path + "에서 디렉토리 생성 실패");
            }
        }
        return path;
    }

    /**
     * 이미지 형식 확인 및 확장자 반환
     */
    private String checkImageFormat(MultipartFile multipartFile) {
        String contentType = multipartFile.getContentType();
        if (ObjectUtils.isEmpty(contentType)) {
            throw new CustomException(UNSUPPORTED_IMAGE_FORMAT);
        } else if (contentType.contains("image/jpeg")) {
            return ".jpg";
        } else if (contentType.contains("image/png")) {
            return ".png";
        } else {
            throw new CustomException(UNSUPPORTED_IMAGE_FORMAT);
        }
    }

    /**
     * ArticleImage 객체 생성
     */
    private ArticleImage buildArticleImage(MultipartFile multipartFile, String path, String newFileName, Article article) {
        ArticleImageRequestDto imageDto = ArticleImageRequestDto.builder()
                .imageName(multipartFile.getOriginalFilename())
                .imageUrl(path + "/" + newFileName)
                .imageSize(multipartFile.getSize())
                .build();
        return new ArticleImage(
                article,
                imageDto.getImageName(),
                imageDto.getImageUrl(),
                imageDto.getImageSize());
    }

    /**
     * 지정된 경로에 파일 저장
     */
    private void saveFile(MultipartFile multipartFile, String path, String newFileName) throws IOException {
        File file = new File(path + "/" + newFileName);
        multipartFile.transferTo(file);
        file.setWritable(true);
        file.setReadable(true);
    }
}
