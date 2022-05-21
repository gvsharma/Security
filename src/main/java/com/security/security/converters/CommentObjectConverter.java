package com.security.security.converters;

import com.security.security.dtos.responses.AuthorResponse;
import com.security.security.dtos.responses.Comment;
import com.security.security.dtos.responses.CommentResponse;
import com.security.security.entities.CommentEntity;
import com.security.security.entities.UserEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentObjectConverter {
    public CommentResponse entityToResponse(CommentEntity commentEntity){
        Comment response = convert(commentEntity);
        return new CommentResponse(response);
    }

    public List<Comment> entityToResponse(List<CommentEntity> commentEntities) {
        return commentEntities.stream().map(CommentObjectConverter::convert).collect(Collectors.toList());
    }

    private static Comment convert(CommentEntity commentEntity) {
        UserEntity author = commentEntity.getAuthor();
        return Comment.builder()
                .id(commentEntity.getId())
                .createdAt(commentEntity.getCreatedAt())
                .updatedAt(commentEntity.getUpdatedAt())
                .body(commentEntity.getBody())
                .author(AuthorResponse.builder()
                        .bio(author.getBio())
                        .username(author.getUsername())
                        .image(author.getImage())
                        .following(false)
                        .build())
                .build();
    }


}
