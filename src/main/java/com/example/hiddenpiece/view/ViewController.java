package com.example.hiddenpiece.view;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/my-page")
    public String myPage() {
        return "my-page";
    }
}

