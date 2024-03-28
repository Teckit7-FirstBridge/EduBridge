package com.ll.edubridge.domain.CommentVoter.repository;

import com.ll.edubridge.domain.CommentVoter.entity.CommentVoter;
import com.ll.edubridge.domain.course.course.entity.Course;
import com.ll.edubridge.domain.member.member.entity.Member;
import com.ll.edubridge.domain.post.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentVoterRepository extends JpaRepository<CommentVoter,Long> {

    public void deleteCommentVoterByCommentAndMember(Comment comment, Member member);
}
