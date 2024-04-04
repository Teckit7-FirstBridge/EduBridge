package com.ll.edubridge.domain.post.comment.repository;

import com.ll.edubridge.domain.member.member.entity.Member;
import com.ll.edubridge.domain.post.comment.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long>,CustomCommentRepository {

    List<Comment> findByPostId(Long postId);

    Page<Comment> findByWriter(Member member, Pageable pageable);
}