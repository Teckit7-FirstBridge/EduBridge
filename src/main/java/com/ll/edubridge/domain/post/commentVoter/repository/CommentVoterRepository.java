package com.ll.edubridge.domain.post.commentVoter.repository;

import com.ll.edubridge.domain.post.commentVoter.entity.CommentVoter;
import com.ll.edubridge.domain.member.member.entity.Member;
import com.ll.edubridge.domain.post.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentVoterRepository extends JpaRepository<CommentVoter,Long> {
    void deleteCommentVoterByCommentAndMember(Comment comment, Member member);
}
