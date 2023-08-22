package com.example.hiddenpiece.service.community;
import com.example.hiddenpiece.domain.dto.community.ArticleRequestDto;
import com.example.hiddenpiece.domain.dto.community.ArticleResponseDto;
import com.example.hiddenpiece.domain.entity.community.Article;
import com.example.hiddenpiece.domain.entity.user.User;
import com.example.hiddenpiece.domain.repository.community.ArticleRepository;
import com.example.hiddenpiece.domain.repository.user.UserRepository;
import com.example.hiddenpiece.exception.CustomException;
import com.example.hiddenpiece.exception.CustomExceptionCode;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long createArticle(String username, ArticleRequestDto params) {
        User loginUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(CustomExceptionCode.INVALID_JWT));

        Article entity = Article.builder()
                .user(loginUser)
                .category(params.getCategory())
                .title(params.getTitle())
                .content(params.getContent())
                .type(params.getType())
                .build();
        articleRepository.save(entity);
        return entity.getArticleId();
    }

    public List<ArticleResponseDto> findAll() {
        Sort sort = Sort.by(Sort.Direction.DESC, "articleId", "createdDate");
        List<Article> list = articleRepository.findAll(sort);
        return list.stream().map(ArticleResponseDto::new).collect(Collectors.toList());
    }

    @Transactional
    public Long updateArticle(String username, final Long articleId, final ArticleRequestDto params) {
        User loginUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(CustomExceptionCode.INVALID_JWT));

        Article target = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("article doesn't exist"));

        if (!target.getUser().equals(loginUser)) {
            throw new CustomException(CustomExceptionCode.NOT_MATCH_WRITER);
        }
        target.modify(params.getTitle(), params.getContent());
        return articleId;
    }

    public Long deleteArticle(String username, final Long articleId) {
        User loginUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(CustomExceptionCode.INVALID_JWT));

        Article target = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("article doesn't exist"));
        if (!target.getUser().equals(loginUser)) {
            throw new CustomException(CustomExceptionCode.NOT_MATCH_WRITER);
        }

        articleRepository.deleteById(articleId);
        return articleId;
    }
}
