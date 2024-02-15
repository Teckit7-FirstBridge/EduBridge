package com.ll.edubridge.domain.post.comment.repository;

import com.ll.edubridge.domain.post.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long>,CustomCommentRepository {

    List<Comment> findByPostId(Long postId);

    List<Comment> findTop2ByPostIdOrderByVoteCountDesc(Long postId);
}
