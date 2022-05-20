package com.security.security.dtos.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class ArticleCreateRequest {
    private Article article;

    @Getter
    public class Article {
        private String title;
        private String description;
        private String body;
        @JsonProperty("tagList")
        private List<String> tags;
    }
}
