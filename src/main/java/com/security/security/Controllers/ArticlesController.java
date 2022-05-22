package com.security.security.Controllers;

import com.security.security.converters.ArticleObjectConverter;
import com.security.security.converters.CommentObjectConverter;
import com.security.security.dtos.responses.ArticleResponse;
import com.security.security.entities.ArticleEntity;
import com.security.security.services.ArticleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ArticlesController {
    private final ArticleService articleService;
    ArticleObjectConverter articleObjectConverter;
    CommentObjectConverter commentObjectConverter;

    public ArticlesController(ArticleService articleService,
                              ArticleObjectConverter articleObjectConverter,
                              CommentObjectConverter commentObjectConverter) {
        this.articleService = articleService;
        this.articleObjectConverter = articleObjectConverter;
        this.commentObjectConverter = commentObjectConverter;
    }

    @GetMapping(value = "/articles", produces = "application/json")
    ResponseEntity<List<ArticleResponse>> getAllArticleList(
            @RequestParam(value = "tag", required = false, defaultValue = "10") String tag,
            @RequestParam(value = "author", required = false, defaultValue = "0") String author,
            @RequestParam(value = "favId", required = false, defaultValue = "10") String favId,
            @RequestParam(value = "offset", required = false, defaultValue = "0") int offset,
            @RequestParam(value = "limit", required = false, defaultValue = "10") int limit
    ) {

        List<ArticleEntity> articles = articleService.getAllArticleList(tag, author, favId, limit, offset);
        List<ArticleResponse> articleResponses = new ArrayList<>();
        articles.forEach(article -> articleResponses.add(articleObjectConverter.entityToResponse(article)));
        return ResponseEntity.ok(articleResponses);
    }


}
