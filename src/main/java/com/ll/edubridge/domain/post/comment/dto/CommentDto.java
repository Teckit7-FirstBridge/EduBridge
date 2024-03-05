package com.ll.edubridge.domain.post.comment.dto;

import com.ll.edubridge.domain.member.member.entity.Member;
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
    private long authorId;
    @NonNull
    private String authorName;
    @NonNull
    private String body;
    @NonNull
    private String postTitle;
    private boolean likedByCurrentUser;
    @NonNull
    private Long postId;

    private int voteCount;

    public CommentDto(Comment comment, Member member) {
        this.id = comment.getId();
        this.createDate = comment.getCreateDate();
        this.authorId = comment.getWriter().getId();
        this.authorName = comment.getWriter().getNickname();
        this.body = comment.getContent();
        this.likedByCurrentUser = comment.getVoter().contains(member);
        this.postId = comment.getPost().getId();
        this.voteCount = comment.getVoteCount();
        this.postTitle = comment.getPost().getTitle();
    }
}
