package com.ll.edubridge.domain.post.comment.controller;

import com.ll.edubridge.domain.member.member.entity.Member;
import com.ll.edubridge.domain.notification.entity.NotificationType;
import com.ll.edubridge.domain.post.comment.dto.CommentDto;
import com.ll.edubridge.domain.post.comment.dto.CreateCommentDto;
import com.ll.edubridge.domain.post.comment.entity.Comment;
import com.ll.edubridge.domain.post.comment.service.CommentService;
import com.ll.edubridge.global.app.AppConfig;
import com.ll.edubridge.global.exceptions.CodeMsg;
import com.ll.edubridge.global.exceptions.GlobalException;
import com.ll.edubridge.global.msg.Msg;
import com.ll.edubridge.global.rq.Rq;
import com.ll.edubridge.global.rsData.RsData;
import com.ll.edubridge.domain.notification.service.NotificationService;
import com.ll.edubridge.standard.base.Empty;
import com.ll.edubridge.standard.base.PageDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/comments", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = " ApiV1CommentController", description = "댓글 CRUD 컨트롤러")
public class ApiV1CommentController {
    private final CommentService commentService;
    private final Rq rq;
    private final NotificationService notificationService;
    @PostMapping("")
    @Operation(summary = "댓글 등록")
    public RsData<CreateCommentDto> createComment(@Valid @RequestBody CreateCommentDto createCommentDto) {
        Comment comment = commentService.create(rq.getMember(), createCommentDto);


        notificationService.notifyComment(comment.getPost().getWriter().getId()); // 댓글 등록 알림
        if(comment.getPost().isPublished()){
            System.out.println("====chanw======true");

            notificationService.createByComment(NotificationType.COMMENT, comment.getPost().getWriter(), comment.getPost(),rq.getMember(),comment); // 알림 내역 저장
        }else{
            System.out.println("====chanw======x");

            notificationService.createByComment(NotificationType.QnA, comment.getPost().getWriter(), comment.getPost(),rq.getMember(),comment); // QnA답변 내역 저장
        }
        return RsData.of(Msg.E200_0_CREATE_SUCCEED.getCode(),
                Msg.E200_0_CREATE_SUCCEED.getMsg(), createCommentDto);
    }

    @GetMapping("/{postId}")
    @Operation(summary = "댓글 목록")
    public RsData<List<CommentDto>> getComments(@PathVariable("postId") Long postId) {
        List<Comment> comments = commentService.findByPostId(postId);

        List<CommentDto> commentDtoList = comments.stream()
                .map((Comment comment) -> new CommentDto(comment, rq.getMember()))
                .toList();

        return RsData.of(Msg.E200_1_INQUIRY_SUCCEED.getCode(),
                Msg.E200_1_INQUIRY_SUCCEED.getMsg(), commentDtoList);
    }

    public record GetCommentResponseBody(@NonNull PageDto<CommentDto> itemPage) {
    }

    @GetMapping("/myList")
    @Operation(summary = "내 댓글 목록")
    public RsData<GetCommentResponseBody> getComments(
            @RequestParam(defaultValue = "1") int page
    ) {

        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page - 1, AppConfig.getBasePageSize(), Sort.by(sorts));
        Page<Comment> comments = commentService.getMyComment(pageable);

        Page<CommentDto> commentPage = comments.map(this::commentToDto);

        return RsData.of(
                Msg.E200_1_INQUIRY_SUCCEED.getCode(),
                Msg.E200_1_INQUIRY_SUCCEED.getMsg(),
                new GetCommentResponseBody(new PageDto<>(commentPage))
        );
    }

    private CommentDto commentToDto(Comment comment) {
        CommentDto dto = new CommentDto(comment, rq.getMember());

        return dto;
    }


    @GetMapping("/{postId}/top")
    @Operation(summary = "추천수 탑2 댓글")
    public RsData<List<CommentDto>> getTopComments(@PathVariable("postId") Long postId) {
        List<Comment> comments = commentService.findTop2(postId);

        List<CommentDto> commentDtoList = comments.stream()
                .map((Comment comment) -> new CommentDto(comment, rq.getMember()))
                .toList();

        return RsData.of(Msg.E200_1_INQUIRY_SUCCEED.getCode(),
                Msg.E200_1_INQUIRY_SUCCEED.getMsg(), commentDtoList);
    }

    @PutMapping("/{postId}/{commentId}")
    @Operation(summary = "댓글 수정")
    public RsData<CommentDto> modifyComment(@PathVariable("commentId") Long commentId, @RequestBody CreateCommentDto createCommentDto) {
        if (!commentService.haveAuthority(commentId))
            throw new GlobalException(CodeMsg.E403_1_NO.getCode(), CodeMsg.E403_1_NO.getMessage());

        Comment modifyComment = commentService.modify(commentId, createCommentDto);

        CommentDto modifyCommentDto = new CommentDto(modifyComment, rq.getMember());

        return RsData.of(Msg.E200_2_MODIFY_SUCCEED.getCode(),
                Msg.E200_2_MODIFY_SUCCEED.getMsg(), modifyCommentDto);
    }

    @DeleteMapping("/{postId}/{commentId}")
    @Operation(summary = "댓글 삭제")
    public RsData<Empty> deleteComment(@PathVariable("commentId") Long commentId) {

        if (!commentService.haveAuthority(commentId))
            throw new GlobalException(CodeMsg.E403_1_NO.getCode(), CodeMsg.E403_1_NO.getMessage());

        commentService.delete(commentId);

        return RsData.of(Msg.E200_3_DELETE_SUCCEED.getCode(),
                Msg.E200_3_DELETE_SUCCEED.getMsg());
    }

    @PostMapping("/{postId}/{commentId}/like")
    @Operation(summary = "댓글 추천")
    public RsData<Void> voteComment(@PathVariable("commentId") Long commentId) {
        Member member = rq.getMember();

        if (!commentService.canLike(member, commentService.getComment(commentId))) {
            throw new GlobalException(CodeMsg.E400_1_ALREADY_RECOMMENDED.getCode(),CodeMsg.E400_1_ALREADY_RECOMMENDED.getMessage());
        }

        commentService.vote(commentId, member);

        return RsData.of(Msg.E200_4_RECOMMEND_SUCCEED.getCode(),
                Msg.E200_4_RECOMMEND_SUCCEED.getMsg());
    }

    @DeleteMapping("/{postId}/{commentId}/like")
    @Operation(summary = "댓글 추천 취소")
    public RsData<Void> deleteVoteComment(@PathVariable("commentId") Long commentId) {
        Member member = rq.getMember();

        if (!commentService.canCancelLike(member, commentService.getComment(commentId))) {
            throw new GlobalException(CodeMsg.E400_2_NOT_RECOMMENDED_YET.getCode(),CodeMsg.E400_2_NOT_RECOMMENDED_YET.getMessage());
        }

        commentService.deleteVote(commentId, member);

        return RsData.of(Msg.E200_5_CANCEL_RECOMMEND_SUCCEED.getCode(),
                Msg.E200_5_CANCEL_RECOMMEND_SUCCEED.getMsg());
    }

}