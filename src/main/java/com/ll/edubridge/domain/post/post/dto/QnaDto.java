package com.ll.edubridge.domain.post.post.dto;

import com.ll.edubridge.domain.post.post.entity.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;

@NoArgsConstructor(access = PROTECTED)
@Getter
public class QnaDto {
    @NonNull
    private long id;
    @NonNull
    private LocalDateTime createDate;
    @NonNull
    private long authorId;
    @NonNull
    private String authorName;
    @NonNull
    private String title;
    @NonNull
    private String body;
    @NonNull
    private int commentCount;

    public QnaDto(Post post) {
        this.id = post.getId();
        this.createDate = post.getCreateDate();
        this.authorId = post.getWriter().getId();
        this.authorName = post.getWriter().getNickname();
        this.title = post.getTitle();
        this.body = post.getContent();
        this.commentCount = post.getCommentList().size();
    }
}
