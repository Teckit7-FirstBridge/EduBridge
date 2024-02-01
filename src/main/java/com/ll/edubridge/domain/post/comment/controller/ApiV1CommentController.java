package com.ll.edubridge.domain.post.comment.controller;

import com.ll.edubridge.domain.member.member.entity.Member;
import com.ll.edubridge.domain.post.comment.dto.CommentDto;
import com.ll.edubridge.domain.post.comment.dto.CreateCommentDto;
import com.ll.edubridge.domain.post.comment.entity.Comment;
import com.ll.edubridge.domain.post.comment.service.CommentService;
import com.ll.edubridge.global.exceptions.GlobalException;
import com.ll.edubridge.global.msg.Msg;
import com.ll.edubridge.global.rq.Rq;
import com.ll.edubridge.global.rsData.RsData;
import com.ll.edubridge.standard.base.Empty;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/comments", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "ApiV1CommentController", description = "댓글 CRUD 컨트롤러")
public class ApiV1CommentController {
    private final CommentService commentService;
    private final Rq rq;

    @PostMapping("")
    @Operation(summary = "댓글 등록")
    public RsData<CreateCommentDto> createComment(@RequestBody CreateCommentDto createCommentDto) {
        Comment comment = commentService.create(rq.getMember(), createCommentDto);

        return RsData.of("200-0", Msg.CREATE.getMsg(), createCommentDto);
    }

    @GetMapping("/{postId}")
    @Operation(summary = "댓글 목록")
    public RsData<List<CommentDto>> getComments(@PathVariable("postId") Long postId) {
        List<Comment> comments = commentService.findByPostId(postId);

        List<CommentDto> commentDtoList = comments.stream()
                .map((Comment comment) -> new CommentDto(comment, rq.getMember()))
                .toList();

        return RsData.of("200-1", Msg.CHECK.getMsg(), commentDtoList);
    }

    @PutMapping("/{postId}/{commentId}")
    @Operation(summary = "댓글 수정")
    public RsData<CommentDto> modifyComment(@PathVariable("commentId") Long commentId, @RequestBody CreateCommentDto createCommentDto) {
        if (!commentService.haveAuthority(commentId))
            throw new GlobalException("403-1", "권한이 없습니다.");

        Comment modifyComment = commentService.modify(commentId, createCommentDto);

        CommentDto modifyCommentDto = new CommentDto(modifyComment, rq.getMember());

        return RsData.of("200-2", Msg.MODIFY.getMsg(), modifyCommentDto);
    }

    @DeleteMapping("/{postId}/{commentId}")
    @Operation(summary = "댓글 삭제")
    public RsData<Empty> deleteComment(@PathVariable("commentId") Long commentId) {

        if (!commentService.haveAuthority(commentId))
            throw new GlobalException("403-1", "권한이 없습니다.");

        commentService.delete(commentId);

        return RsData.of("200-3", Msg.DELETE.getMsg());
    }

    @PostMapping("/{postId}/{commentId}/like")
    @Operation(summary = "댓글 추천")
    public RsData<Void> voteComment(@PathVariable("commentId") Long commentId) {
        Member member = rq.getMember();

        if (!commentService.canLike(member, commentService.getComment(commentId))) {
            throw new GlobalException("400-1", "이미 추천하셨습니다.");
        }

        commentService.vote(commentId, member);

        return RsData.of("200-4", Msg.RECOMMEND.getMsg());
    }

    @DeleteMapping("/{postId}/{commentId}/like")
    @Operation(summary = "댓글 추천 취소")
    public RsData<Void> deleteVoteComment(@PathVariable("commentId") Long commentId) {
        Member member = rq.getMember();

        if (!commentService.canCancelLike(member, commentService.getComment(commentId))) {
            throw new GlobalException("400-2", "추천을 하지 않았습니다.");
        }

        commentService.deleteVote(commentId, member);

        return RsData.of("200-5", Msg.RECOMMENDCANCEL.getMsg());
    }
}