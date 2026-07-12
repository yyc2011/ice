package com.ice.controller;

import com.ice.auth.AuthContext;
import com.ice.dto.article.ArticleDetailResponse;
import com.ice.dto.article.CreateDraftRequest;
import com.ice.dto.article.CreateDraftResponse;
import com.ice.dto.article.FeaturedArticlesResponse;
import com.ice.dto.article.PublishResponse;
import com.ice.dto.article.ReviewAppealRequest;
import com.ice.dto.article.ReviewAppealResponse;
import com.ice.service.ArticleService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/articles")
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @PostMapping("/drafts")
    @ResponseStatus(HttpStatus.CREATED)
    public CreateDraftResponse createDraft(@RequestBody CreateDraftRequest request) {
        return articleService.createDraft(AuthContext.requireUserId(), request);
    }

    @PutMapping("/{id}")
    public CreateDraftResponse updateDraft(
            @PathVariable long id,
            @RequestBody CreateDraftRequest request
    ) {
        return articleService.updateDraft(AuthContext.requireUserId(), id, request);
    }

    @PostMapping("/{id}/publish")
    public PublishResponse publish(@PathVariable long id) {
        return articleService.publish(AuthContext.requireUserId(), id);
    }

    @GetMapping("/featured")
    public FeaturedArticlesResponse featured(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return articleService.listFeatured(
                Math.max(page, 1),
                Math.min(Math.max(size, 1), 50)
        );
    }

    @GetMapping("/{id}")
    public ArticleDetailResponse getArticle(@PathVariable long id) {
        return articleService.getArticle(AuthContext.requireUserId(), id);
    }

    @PostMapping("/{id}/review-appeal")
    public ReviewAppealResponse reviewAppeal(
            @PathVariable long id,
            @RequestBody ReviewAppealRequest request
    ) {
        return articleService.reviewAppeal(AuthContext.requireUserId(), id, request);
    }
}
