package com.ll.edubridge.domain.post.comment.controller;

import com.ll.edubridge.domain.post.comment.dto.CommentDto;
import com.ll.edubridge.domain.post.comment.dto.CreateCommentDto;
import com.ll.edubridge.domain.post.comment.entity.Comment;
import com.ll.edubridge.domain.post.comment.service.CommentService;
import com.ll.edubridge.global.exceptions.GlobalException;
import com.ll.edubridge.global.rq.Rq;
import com.ll.edubridge.global.rsData.RsData;
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

        return RsData.of("200-0", "등록 성공", createCommentDto);
    }

    @GetMapping("/{postId}")
    @Operation(summary = "댓글 목록")
    public RsData<List<CommentDto>> getComments(@PathVariable("postId")Long postId) {
        List<Comment> comments = commentService.findByPostId(postId);

        List<CommentDto> commentDtoList = comments.stream()
                .map((Comment comment) -> new CommentDto(comment, rq.getMember()))
                .toList();

        return RsData.of("200-1", "조회 성공", commentDtoList);
    }

    @PutMapping("/{postId}")
    @Operation(summary = "댓글 수정")
    public RsData<CommentDto> modifyComment(@PathVariable("postId") Long postId, @RequestBody CreateCommentDto createCommentDto) {
        if(!commentService.haveAuthority(postId))
            throw new GlobalException("403-1", "권한이 없습니다.");

        Comment modifyComment = commentService.modify(postId, createCommentDto);

        CommentDto modifyCommentDto = new CommentDto(modifyComment, rq.getMember());

        return RsData.of("200-2", "수정 성공", modifyCommentDto);
    }
}