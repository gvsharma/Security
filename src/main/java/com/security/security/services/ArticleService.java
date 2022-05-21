package com.security.security.services;

import com.security.security.dtos.requests.ArticleUpdateRequest;
import com.security.security.dtos.requests.CommentCreateRequest;
import com.security.security.entities.ArticleEntity;
import com.security.security.entities.CommentEntity;
import com.security.security.entities.TagEntity;
import com.security.security.entities.UserEntity;
import com.security.security.repositories.ArticleRepository;
import com.security.security.repositories.CommentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

import static java.util.Objects.isNull;

@Service
public class ArticleService {
    private ArticleRepository articleRepo;
    private CommentRepository commentRepo;
    private TagsService tagsService;

    public static class AccessDeniedExceptoin extends RuntimeException {
        public AccessDeniedExceptoin(){super("Access Denied");}
    }

    public ArticleService(ArticleRepository articleRepo, CommentRepository commentRepo, TagsService tagsService){
        this.articleRepo = articleRepo;
        this.commentRepo = commentRepo;
        this.tagsService = tagsService;
    }

    public List<ArticleEntity> getAllArticles(int limit, int offset) {
        PageRequest pr = PageRequest.of(offset/limit, offset);
        return articleRepo.findAll(pr).getContent();
    }

    public ArticleEntity getArticleBySlug(String slug) {
        return articleRepo.findArticleEntityBySlug(slug);
    }

    public String getSlug(String title) {
        return title.toLowerCase(Locale.ROOT).replace(' ', '-');
    }

    public ArticleEntity createArticle(String articleTitle, String articleDescription, String articleBody,
                                       List<String> articleTagList, UserEntity currentLoggedInUser) {
        List<TagEntity> tagEntities = tagsService.createTags(articleTagList);
        String articleSlug = getSlug(articleTitle);
        ArticleEntity articleEntity = ArticleEntity.builder()
                .slug(articleSlug)
                .title(articleTitle)
                .body(articleBody)
                .description(articleDescription)
                .tags(tagEntities)
                .author(currentLoggedInUser).build();
        return articleRepo.save(articleEntity);
    }

    public List<ArticleEntity> getArticleFeed(UserEntity userEntity, int limit, int offset) {
        PageRequest pr = PageRequest.of(offset/limit, limit);
        return articleRepo.findArticleFeed(userEntity.getId(), pr);
    }

    public List<ArticleEntity> getAllArticleList(String tag, String author, String favorited, int limit, int offset) {
        PageRequest pr = PageRequest.of(offset/limit, limit);
        return articleRepo.findByAuthorOrTagOrFavorited(tag, author, favorited, pr);
    }

    public ArticleEntity updateArticle(ArticleUpdateRequest.Article article, String slug, UserEntity user) {
        ArticleEntity oldArticle = articleRepo.findArticleEntityBySlug(slug);
        if(oldArticle.getAuthor().getId() != user.getId()){
            throw new AccessDeniedExceptoin();
        }
        if(!isNull(article.getTitle()))  {
            oldArticle.setTitle(article.getTitle());
            oldArticle.setSlug(getSlug(article.getTitle()));
        }
        if(!isNull(article.getBody())) oldArticle.setBody(article.getBody());
        if(!isNull(article.getDescription())) oldArticle.setDescription(article.getDescription());
        return articleRepo.save(oldArticle);
    }

    public void deleteArticleBySlug(String slug, UserEntity user){
        ArticleEntity article = articleRepo.findArticleEntityBySlug(slug);
        if(article.getAuthor().getId() != user.getId()) {
            throw new AccessDeniedExceptoin();
        }
        articleRepo.deleteBySlug(slug);
    }

    public CommentEntity addCommentToArticle(String slug, CommentCreateRequest.Comment body, UserEntity currentLoggedInUser){
        ArticleEntity article = articleRepo.findArticleEntityBySlug(slug);
        CommentEntity comment = CommentEntity.builder()
                .body(body.getBody())
                .author(currentLoggedInUser)
                .article(article).build();
        return commentRepo.save(comment);
    }

    public List<CommentEntity> getAllCommentToArticle(String slug) {
        ArticleEntity article = getArticleBySlug(slug);
        return commentRepo.findCommentEntityByArticle(article);
    }

    public void deleteComment(long id, UserEntity userEntity){
        CommentEntity comment = commentRepo.getOne(id);
        if (userEntity.getId() != comment.getAuthor().getId())
            throw new AccessDeniedExceptoin();
        commentRepo.deleteById(id);
    }

    public ArticleEntity favoriteArticle(String slug, UserEntity user) {
        ArticleEntity articleEntity = getArticleBySlug(slug);
        articleEntity.getFans().add(user);
        return articleRepo.saveAndFlush(articleEntity);
    }

    public ArticleEntity unFavouriteArticle(String slug, UserEntity user){
        ArticleEntity articleEntity = getArticleBySlug(slug);
        articleRepo.unfavoriteArticle(user.getId(), articleEntity.getId());
        return articleEntity;
    }
}
