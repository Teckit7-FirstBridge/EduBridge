package com.ll.edubridge.domain.post.comment.service;

import com.ll.edubridge.domain.member.member.entity.Member;
import com.ll.edubridge.domain.notification.service.NotificationService;
import com.ll.edubridge.domain.post.comment.dto.CreateCommentDto;
import com.ll.edubridge.domain.post.comment.entity.Comment;
import com.ll.edubridge.domain.post.comment.repository.CommentRepository;
import com.ll.edubridge.domain.post.post.service.PostService;
import com.ll.edubridge.global.exceptions.CodeMsg;
import com.ll.edubridge.global.exceptions.GlobalException;
import com.ll.edubridge.global.rq.Rq;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {
    private final CommentRepository commentRepository;
    private final NotificationService notificationService;
    private final Rq rq;
    private final PostService postService;

    public Page<Comment> getMyComment(Pageable pageable) {
        Member member = rq.getMember();

        return commentRepository.findByWriter(member, pageable);
    }

    // 권한 검사 기능
    public boolean haveAuthority(Long id) {

        Member member = rq.getMember();

        Comment comment = this.getComment(id);

        if (member == null) return false;

        return comment.getWriter().equals(member);
    }



    // 댓글 작성 기능
    @Transactional
    public Comment create(Member author, CreateCommentDto createCommentDto) {
        Comment comment = Comment.builder()
                .writer(author)
                .content(createCommentDto.getBody())
                .post(postService.getPost(createCommentDto.getPostId()))
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

        notificationService.deleteByComment(comment);

        commentRepository.delete(comment);
    }


    // 댓글 조회 기능
    public Comment getComment(Long id) {
        Optional<Comment> comment = this.findById(id);
        if (comment.isPresent()) {
            return comment.get();
        } else {
            throw new GlobalException(CodeMsg.E404_1_DATA_NOT_FIND.getCode(),CodeMsg.E404_1_DATA_NOT_FIND.getMessage());
        }
    }

    public Optional<Comment> findById(Long id) {
        return commentRepository.findById(id);
    }


    public List<Comment> findByPostId(Long postId) {
        return commentRepository.findByPostId(postId);
    }

    public List<Comment> findTop2(Long postId) {
        return commentRepository.findBestComment(postId);
    }
}
