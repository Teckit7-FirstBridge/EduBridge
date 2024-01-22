package com.ll.edubridge.domain.post.comment.service;

import com.ll.edubridge.domain.member.member.entity.Member;
import com.ll.edubridge.domain.post.comment.entity.Comment;
import com.ll.edubridge.domain.post.comment.repository.CommentRepository;
import com.ll.edubridge.global.exceptions.GlobalException;
import com.ll.edubridge.global.rq.Rq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {
    private final CommentRepository commentRepository;
    private final Rq rq;

    // 권한 검사 기능
    public boolean haveAuthority(Comment comment){

        Member member=rq.getMember();

        if(member==null) return false;

        return comment.getWriter().equals(member);
    }

    // 댓글 추천 권한 검사
    public boolean canLike(Member member, Comment comment){
        if(member==null) {
            return false;
        }
        return !comment.getVoter().contains(member);
    }

    // 댓글 취소 권한 검사
    public boolean canCancelLike(Member member, Comment comment){
        if(member==null){
            return false;
        }
        return comment.getVoter().contains(member);
    }

    // 댓글 작성 기능
    @Transactional
    public void create(Member author, String content) {
        Comment comment = Comment.builder()
                .writer(author)
                .content(content)
                .build();

        commentRepository.save(comment);
    }

    // 댓글 수정 기능
    @Transactional
    public void modify(Long commentId, String newContent) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new GlobalException("404-1", "댓글을 찾을 수 없습니다.");

        comment.setContent(newContent);
    }

    // 댓글 삭제 기능
    @Transactional
    public void delete(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new GlobalException("404-1", "댓글을 찾을 수 없습니다.");

        commentRepository.delete(comment);
    }
    // 댓글 추천 기능
    @Transactional
    public void vote(Long id, Member member) {
        Comment comment = this.getComment(id);
        comment.getVoter().add(member);
        commentRepository.save(comment);
    }

    // 댓글 추천 취소 기능
    @Transactional
    public void deleteVote(Long id, Member member) {
        Comment comment = this.getComment(id);
        comment.getVoter().remove(member);
        commentRepository.save(comment);
    }

    // 댓글 조회 기능
    private Comment getComment(Long id) {
        Optional<Comment> comment = this.commentRepository.findById(id);
        if(comment.isPresent()) {
            return comment.get();
        } else {
            throw new GlobalException("404-1", "comment를 찾을 수 없습니다");
        }
    }

    private Optional<Comment> findById(Long id) {
        return commentRepository.findById(id);
    }
}
