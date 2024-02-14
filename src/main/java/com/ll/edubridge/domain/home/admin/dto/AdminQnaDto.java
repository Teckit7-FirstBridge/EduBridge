package com.ll.edubridge.domain.home.admin.dto;

import com.ll.edubridge.domain.post.post.entity.Post;
import lombok.Getter;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;

@Getter
public class AdminQnaDto {
    @NonNull
    private long id;
    @NonNull
    private LocalDateTime createDate;
    @NonNull
    private String authorName;
    @NonNull
    private String title;
    @NonNull
    private int commentCount;


    public AdminQnaDto(Post post) {
        this.id = post.getId();
        this.createDate = post.getCreateDate();
        this.authorName = post.getWriter().getNickname();
        this.title = post.getTitle();
        this.commentCount = post.getCommentList().size();
    }
}
