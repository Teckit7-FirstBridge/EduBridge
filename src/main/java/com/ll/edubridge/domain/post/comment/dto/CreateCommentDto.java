package com.ll.edubridge.domain.post.comment.dto;

import com.ll.edubridge.domain.post.comment.entity.Comment;
import lombok.Getter;
import org.springframework.lang.NonNull;

@Getter
public class CreateCommentDto {
    @NonNull
    private String body;
    @NonNull
    private Long postId;

    public CreateCommentDto(Comment comment) {
        this.body = comment.getContent();
        this.postId=comment.getPost().getId();
    }

    public CreateCommentDto(){

    }
}
