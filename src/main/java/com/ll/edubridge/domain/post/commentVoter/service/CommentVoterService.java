package com.ll.edubridge.domain.post.commentVoter.service;

import com.ll.edubridge.domain.member.member.entity.Member;
import com.ll.edubridge.domain.post.comment.entity.Comment;
import com.ll.edubridge.domain.post.commentVoter.entity.CommentVoter;
import com.ll.edubridge.domain.post.commentVoter.repository.CommentVoterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentVoterService {
    private final CommentVoterRepository commentVoterRepository;

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
