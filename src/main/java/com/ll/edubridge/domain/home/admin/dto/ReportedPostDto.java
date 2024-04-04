package com.ll.edubridge.domain.home.admin.dto;

import com.ll.edubridge.domain.post.post.entity.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class ReportedPostDto {
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

    public ReportedPostDto(Post post) {
        this.id = post.getId();
        this.createDate = post.getCreateDate();
        this.authorId = post.getWriter().getId();
        this.authorName = post.getWriter().getUsername();
        this.title = post.getTitle();
    }
}
