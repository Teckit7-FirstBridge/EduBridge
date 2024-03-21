package com.ll.edubridge.domain.CommentVoter.service;

import com.ll.edubridge.domain.CommentVoter.entity.CommentVoter;
import com.ll.edubridge.domain.CommentVoter.repository.CommentVoterRepository;
import com.ll.edubridge.domain.CourseVoter.entity.CourseVoter;
import com.ll.edubridge.domain.CourseVoter.repositry.CourseVoterRepository;
import com.ll.edubridge.domain.course.course.entity.Course;
import com.ll.edubridge.domain.course.course.repository.CourseRepository;
import com.ll.edubridge.domain.member.member.entity.Member;
import com.ll.edubridge.domain.post.comment.entity.Comment;
import com.ll.edubridge.domain.post.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentVoterService {
    private final CommentVoterRepository commentVoterRepository;
    private final CommentRepository commentRepository;

    public Boolean isVote(Member member, Comment comment){
        return false;
    }
    public void vote(Member member, Comment comment){

        CommentVoter commentVoter = new CommentVoter(member, comment);
        commentVoterRepository.save(commentVoter);
    }

    @Transactional
    public void deleteVote(Comment comment, Member member){

        commentVoterRepository.deleteCommentVoterByCommentAndMember(comment,member);
    }

    public boolean canLike(Member member, Comment comment) {
        if (member == null) return false;
        if (comment == null) return false;
        return !comment.getCommentVoters().contains(new CommentVoter(member,comment));
    }
    public boolean canCancelLike(Member member, Comment comment) {
        if (member == null) return false;
        if (comment == null) return false;

        return comment.getCommentVoters().contains(new CommentVoter(member,comment));
    }

}
