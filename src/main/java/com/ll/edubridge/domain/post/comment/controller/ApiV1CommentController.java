package com.ll.edubridge.domain.post.comment.controller;

import com.ll.edubridge.domain.member.member.entity.Member;
import com.ll.edubridge.domain.post.comment.dto.CommentDto;
import com.ll.edubridge.domain.post.comment.dto.CreateCommentDto;
import com.ll.edubridge.domain.post.comment.entity.Comment;
import com.ll.edubridge.domain.post.comment.service.CommentService;
import com.ll.edubridge.global.exceptions.CodeMsg;
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

        return RsData.of(Msg.E200_0_CREATE_SUCCUSSED.getCode(),
                Msg.E200_0_CREATE_SUCCUSSED.getMsg(), createCommentDto);
    }

    @GetMapping("/{postId}")
    @Operation(summary = "댓글 목록")
    public RsData<List<CommentDto>> getComments(@PathVariable("postId") Long postId) {
        List<Comment> comments = commentService.findByPostId(postId);

        List<CommentDto> commentDtoList = comments.stream()
                .map((Comment comment) -> new CommentDto(comment, rq.getMember()))
                .toList();

        return RsData.of(Msg.E200_1_INQUIRY_SUCCUSSED.getCode(),
                Msg.E200_1_INQUIRY_SUCCUSSED.getMsg(), commentDtoList);
    }

    @PutMapping("/{postId}/{commentId}")
    @Operation(summary = "댓글 수정")
    public RsData<CommentDto> modifyComment(@PathVariable("commentId") Long commentId, @RequestBody CreateCommentDto createCommentDto) {
        if (!commentService.haveAuthority(commentId))
            throw new GlobalException(CodeMsg.E403_1_NO.getCode(), CodeMsg.E403_1_NO.getMessage());

            Comment modifyComment = commentService.modify(commentId, createCommentDto);

        CommentDto modifyCommentDto = new CommentDto(modifyComment, rq.getMember());

        return RsData.of(Msg.E200_2_MODIFY_SUCCUSSED.getCode(),
                Msg.E200_2_MODIFY_SUCCUSSED.getMsg(), modifyCommentDto);
    }

    @DeleteMapping("/{postId}/{commentId}")
    @Operation(summary = "댓글 삭제")
    public RsData<Empty> deleteComment(@PathVariable("commentId") Long commentId) {

        if (!commentService.haveAuthority(commentId))
            throw new GlobalException(CodeMsg.E403_1_NO.getCode(), CodeMsg.E403_1_NO.getMessage());

        commentService.delete(commentId);

        return RsData.of(Msg.E200_3_DELETE_SUCCUSSED.getCode(),
                Msg.E200_3_DELETE_SUCCUSSED.getMsg());
    }

    @PostMapping("/{postId}/{commentId}/like")
    @Operation(summary = "댓글 추천")
    public RsData<Void> voteComment(@PathVariable("commentId") Long commentId) {
        Member member = rq.getMember();

        if (!commentService.canLike(member, commentService.getComment(commentId))) {
            throw new GlobalException(CodeMsg.E400_1_ALREADY_RECOMMENDED.getCode(),CodeMsg.E400_1_ALREADY_RECOMMENDED.getMessage());
        }

        commentService.vote(commentId, member);

        return RsData.of(Msg.E200_4_RECOMMEND_SUCCUSSED.getCode(),
                Msg.E200_4_RECOMMEND_SUCCUSSED.getMsg());
    }

    @DeleteMapping("/{postId}/{commentId}/like")
    @Operation(summary = "댓글 추천 취소")
    public RsData<Void> deleteVoteComment(@PathVariable("commentId") Long commentId) {
        Member member = rq.getMember();

        if (!commentService.canCancelLike(member, commentService.getComment(commentId))) {
            throw new GlobalException(CodeMsg.E400_2_NOT_RECOMMENDED_YET.getCode(),CodeMsg.E400_2_NOT_RECOMMENDED_YET.getMessage());
        }

        commentService.deleteVote(commentId, member);

        return RsData.of(Msg.E200_5_CANCEL_RECOMMEND_SUCCUSSED.getCode(),
                Msg.E200_5_CANCEL_RECOMMEND_SUCCUSSED.getMsg());
    }
}