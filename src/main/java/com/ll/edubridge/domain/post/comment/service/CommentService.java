package com.ll.edubridge.domain.post.comment.service;

import com.ll.edubridge.domain.member.member.entity.Member;
import com.ll.edubridge.domain.post.comment.dto.CreateCommentDto;
import com.ll.edubridge.domain.post.comment.entity.Comment;
import com.ll.edubridge.domain.post.comment.repository.CommentRepository;
import com.ll.edubridge.global.exceptions.GlobalException;
import com.ll.edubridge.global.rq.Rq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {
    private final CommentRepository commentRepository;
    private final Rq rq;

    // 권한 검사 기능
    public boolean haveAuthority(Long id) {

        Member member = rq.getMember();

        Comment comment = this.getComment(id);

        if (member == null) return false;

        return comment.getWriter().equals(member);
    }

    // 댓글 추천 권한 검사
    public boolean canLike(Member member, Comment comment) {
        if (member == null) {
            return false;
        }
        return !comment.getVoter().contains(member);
    }

    // 댓글 취소 권한 검사
    public boolean canCancelLike(Member member, Comment comment) {
        if (member == null) {
            return false;
        }
        return comment.getVoter().contains(member);
    }

    // 댓글 작성 기능
    @Transactional
    public Comment create(Member author, CreateCommentDto createCommentDto) {
        Comment comment = Comment.builder()
                .writer(author)
                .content(createCommentDto.getBody())
                .build();

        return commentRepository.save(comment);
    }

    // 댓글 수정 기능
    @Transactional
    public Comment modify(Long commentId, CreateCommentDto createCommentDto) {
        Comment comment = this.getComment(commentId);

        comment.setContent(createCommentDto.getBody());

        return commentRepository.save(comment);
    }

    // 댓글 삭제 기능
    @Transactional
    public void delete(Long commentId) {
        Comment comment = this.getComment(commentId);

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
    public Comment getComment(Long id) {
        Optional<Comment> comment = this.findById(id);
        if (comment.isPresent()) {
            return comment.get();
        } else {
            throw new GlobalException("404-1", "comment를 찾을 수 없습니다");
        }
    }

    public Optional<Comment> findById(Long id) {
        return commentRepository.findById(id);
    }


    public List<Comment> findByPostId(Long postId) {
        return commentRepository.findByPostId(postId);
    }
}

