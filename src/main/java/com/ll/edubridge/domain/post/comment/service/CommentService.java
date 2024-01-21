package com.ll.edubridge.domain.post.comment.service;

import com.ll.edubridge.domain.member.member.entity.Member;
import com.ll.edubridge.domain.post.comment.entity.Comment;
import com.ll.edubridge.domain.post.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {
    private final CommentRepository commentRepository;

    // 댓글 작성 기능
    @Transactional
    public void CommentWrite(Member author, String content) {
        Comment comment = Comment.builder()
                // .author(author)
                .content(content)
                .build();

        commentRepository.save(comment);
    }

    // 댓글 조회 기능
    public Comment getCommentById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다. commentId: " + commentId));
    }

    // 댓글 수정 기능
    @Transactional
    public void updateComment(Long commentId, String newContent) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다. commentId: " + commentId));

        comment.setContent(newContent);
    }

    // 댓글 삭제 기능
    @Transactional
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다. commentId: " + commentId));

        commentRepository.delete(comment);
    }

    // 댓글 추천 기능
    @Transactional
    public void addCommentLike(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다. commentId: " + commentId));

        int currentLikes = comment.getLikes();
        comment.setLikes(currentLikes + 1);
    }
}
