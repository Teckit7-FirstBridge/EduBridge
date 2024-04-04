package com.ll.edubridge.domain.post.post.dto;

import com.ll.edubridge.domain.member.member.entity.Member;
import com.ll.edubridge.domain.post.post.entity.Post;
import com.ll.edubridge.domain.post.postVoter.entity.PostVoter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;

@NoArgsConstructor(access = PROTECTED)
@Getter
public class PostDto {
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
    private int voteCount;
    @NonNull
    private boolean likedByCurrentUser;
    @NonNull
    private boolean isReport;
    @NonNull
    private int commentCount;

    public PostDto(Post post, Member member) {
        this.id = post.getId();
        this.createDate = post.getCreateDate();
        this.authorId = post.getWriter().getId();
        this.authorName = post.getWriter().getNickname();
        this.title = post.getTitle();
        this.body = post.getContent();
        this.voteCount = post.getPostVoters().size();
        this.likedByCurrentUser = post.getPostVoters().contains(new PostVoter(post,member));
        this.isReport = post.isReport();
        this.commentCount = post.getCommentList().size();
    }
}
