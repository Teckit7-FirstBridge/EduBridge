package com.ll.edubridge.domain.post.comment.repository;

import com.ll.edubridge.domain.post.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
