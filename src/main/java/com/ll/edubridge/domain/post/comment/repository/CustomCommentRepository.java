package com.ll.edubridge.domain.post.comment.repository;

import com.ll.edubridge.domain.post.comment.entity.Comment;

import java.util.List;

public interface CustomCommentRepository {

    List<Comment> findBestComment(Long postId);
}
