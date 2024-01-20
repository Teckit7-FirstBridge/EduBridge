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

    @Transactional
    public void CommentWrite(Member author, String content) {
        Comment comment = Comment.builder()
                // .author(author)
                .content(content)
                .build();

        commentRepository.save(comment);
    }

    public Comment getCommentById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다. commentId: " + commentId));
    }


}
