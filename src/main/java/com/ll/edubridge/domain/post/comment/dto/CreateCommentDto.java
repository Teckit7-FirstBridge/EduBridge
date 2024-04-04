package com.ll.edubridge.domain.post.comment.dto;

import com.ll.edubridge.domain.post.comment.entity.Comment;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class CreateCommentDto {
    @NotEmpty
    @Size(max = 500)
    private String body;

    private Long postId;

    public CreateCommentDto(Comment comment) {
        this.body = comment.getContent();
        this.postId = comment.getPost().getId();
    }

    public CreateCommentDto(){}
}
