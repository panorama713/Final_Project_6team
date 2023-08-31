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

    @GetMapping("articles")
    public String createArticles() {
        return "article-write";
    }

    @GetMapping("articles/list")
    public String articleList() {
        return "article-list";
    }

    @GetMapping("/articles/{articleId}")
    public String articleDetail() {
        return "article-detail";
    }

    @GetMapping("/articles/edit/{articleId}")
    public String articleUpdate() {
        return "article-update";
    }

}

