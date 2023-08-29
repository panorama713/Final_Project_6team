package com.example.hiddenpiece.view;

import com.example.hiddenpiece.domain.dto.community.article.ArticleRequestDto;
import com.example.hiddenpiece.service.community.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/views")
public class ViewController {
    private final ArticleService articleService;
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/main")
    public String mainView() {
        return "main";
    }

    @GetMapping("/signup")
    public String signup() {
        return "sign-up";
    }


    /* 커뮤니티 */

    @GetMapping("/article")
    public String createArticles() {
        return "article-write";
    }

    @PostMapping("/article")
    public String createArticle(
            Authentication authentication, @RequestBody ArticleRequestDto params
    ) {
        String username = authentication.getName();
        articleService.createArticle(username, params);
        return "article-write";
    }

    @GetMapping("/articlelist")
    public String readAllArticles(Model model) {
        model.addAttribute("articles", articleService.readArticles());
        return "article-list";
    }

    @GetMapping("/article/{articleId}")
    public String readArticle(Model model, @PathVariable final Long articleId) {
        model.addAttribute("article",articleService.readArticle(articleId));
        return "article-detail";
    }


    @GetMapping("/article/get/{articleId}")
    public String updateArticle(Model model, @PathVariable final Long articleId) {
        model.addAttribute("article",articleService.readArticle(articleId));
        return "article-update";
    }

    @PutMapping("/article/{articleId}")
    public String updateArticle(
            Authentication authentication,
            @PathVariable final Long articleId,
            @RequestBody final ArticleRequestDto params) {
        String username = authentication.getName();
        articleService.updateArticle(username, articleId, params);
        return "article-update";
    }

    @DeleteMapping("/article/{articleId}")
    public String deleteArticle(Authentication authentication, @PathVariable final Long articleId) {
        String username = authentication.getName();
        articleService.deleteArticle(username, articleId);
        return "redirect:/views/articlelist";
    }

}

