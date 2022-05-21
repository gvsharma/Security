package com.security.security.converters;


import com.security.security.dtos.responses.Article;
import com.security.security.dtos.responses.ArticleResponse;
import com.security.security.dtos.responses.AuthorResponse;
import com.security.security.entities.ArticleEntity;
import com.security.security.entities.UserEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleObjectConverter {
    public ArticleResponse entityToResponse(ArticleEntity article){
        List<String> tagNames = new ArrayList<>();
        article.getTags().forEach(tag -> tagNames.add(tag.getName()));

        UserEntity user = article.getAuthor();

        AuthorResponse authorResponse = AuthorResponse.builder()
                .bio(user.getBio())
                .username(user.getUsername())
                .image(user.getImage())
                .following(true).build();

        return new ArticleResponse(Article.builder()
                .title(article.getTitle())
                .body(article.getBody())
                .description(article.getDescription())
                .tagList(tagNames)
                .createdAt(article.getCreatedAt())
                .updatedAt(article.getUpdatedAt())
                .favoritesCount(0)
                .favorited(false)
                .author(authorResponse)
                .build());
    }
}
