package com.ll.edubridge.domain.post.comment.dto;

import com.ll.edubridge.domain.post.comment.entity.Comment;
import lombok.Getter;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;

@Getter
public class CommentDto {
    @NonNull
    private long id;
    @NonNull
    private LocalDateTime createDate;
    @NonNull
    private LocalDateTime modifyDate;
    @NonNull
    private long authorId;
    @NonNull
    private String authorName;
    @NonNull
    private String title;
    @NonNull
    private String body;

    public CommentDto(Comment comment) {
        this.id = comment.getId();
        this.createDate = comment.getCreateDate();
        this.authorId = comment.getWriter().getId();
        this.authorName = comment.getWriter().getUsername();
        this.body = comment.getContent()
    }
}
