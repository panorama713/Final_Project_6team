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

    @GetMapping("/my-roadmap")
    public String myRoadmap() {
        return "my-roadmap";
    }

    @GetMapping("articles")
    public String createArticles() {
        return "article-write";
    }

    @GetMapping("articles/list")
    public String articleList() {
        return "article-list";
    }

    @GetMapping("/user-profile")
    public String viewUserProfile() { return "user-profile"; }

    @GetMapping("/articles/{articleId}")
    public String articleDetail() {
        return "article-detail";
    }

    @GetMapping("/articles/edit/{articleId}")
    public String articleUpdate() {
        return "article-update";
    }

    @GetMapping("/find/username")
    public String findUsernamePage() {
        return "find-username";
    }

    @GetMapping("/find/password")
    public String findPasswordPage() {
        return "find-password";
    }

    @GetMapping("/change-password")
    public String changePasswordPage() {
        return "change-password";
    }

    @GetMapping("/search")
    public String totalSearch() {
        return "search-items";
    }

    @GetMapping("/search/roadmaps")
    public String searchRoadmaps() {
        return "search-roadmaps";
    }

    @GetMapping("/my-page/profile")
    public String myProfile() {
        return "my-profile";
    }

    @GetMapping("/my-page/articles")
    public String myArticles() {return "my-profile-articles";}

    @GetMapping("/my-page/comments")
    public String myComments() {return "my-profile-comments";}

    @GetMapping("/my-page/bookmarks")
    public String myBookmarks() {return "my-profile-bookmarks";}

    @GetMapping("/my-page/update-info")
    public String updateInfo() {
        return "update-info";
    }

    @GetMapping("/my-page/withdrawal")
    public String deleteUser() {
        return "withdrawal";
    }

    @GetMapping("/my-page/roadmaps/bookmarks")
    public String myRoadmapBookmark() {
        return "my-profile-roadmap-bookmark";
    }

    @GetMapping("/roadmaps/{roadmapId}/elements")
    public String roadmapElement() {
        return "roadmap-element";
    }

    @GetMapping("/bookmarks/roadmaps")
    public String roadmapBookmark() {
        return "roadmap-bookmark";
    }

    @GetMapping("/roadmaps/{roadmapId}")
    public String readRoadmap() {
        return "user-roadmap";
    }

    @GetMapping("/my-page/following")
    public String followingUser() {
        return "my-following";
    }

    @GetMapping("/my-page/follower")
    public String followerUser() {
        return "my-follower";
    }

    @GetMapping("/my-page/following/articles")
    public String readFollowUserArticles() {
        return "my-following-articles";
    }

    @GetMapping("/my-page/following/roadmaps")
    public String readFollowUserRoadmaps() {
        return "my-following-roadmaps";
    }

    @GetMapping("/my-page/roadmaps")
    public String readMyPageRoadmaps() {
        return "my-page-roadmaps";
    }
}

