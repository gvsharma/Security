package com.security.security.repositories;

import com.security.security.entities.ArticleEntity;
import com.security.security.entities.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    List<CommentEntity> findCommentEntityByArticle(ArticleEntity articleEntity);
}
