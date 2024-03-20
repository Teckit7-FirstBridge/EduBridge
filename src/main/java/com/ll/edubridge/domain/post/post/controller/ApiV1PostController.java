package com.ll.edubridge.domain.post.post.controller;

import com.ll.edubridge.domain.PostVoter.entity.PostVoter;
import com.ll.edubridge.domain.PostVoter.service.PostVoterService;
import com.ll.edubridge.domain.member.member.entity.Member;
import com.ll.edubridge.domain.post.post.dto.CreatePostDto;
import com.ll.edubridge.domain.post.post.dto.PostDto;
import com.ll.edubridge.domain.post.post.dto.QnaDto;
import com.ll.edubridge.domain.post.post.entity.Post;
import com.ll.edubridge.domain.post.post.service.PostService;
import com.ll.edubridge.global.app.AppConfig;
import com.ll.edubridge.global.exceptions.CodeMsg;
import com.ll.edubridge.global.exceptions.GlobalException;
import com.ll.edubridge.global.msg.Msg;
import com.ll.edubridge.global.rq.Rq;
import com.ll.edubridge.global.rsData.RsData;
import com.ll.edubridge.standard.base.Empty;
import com.ll.edubridge.standard.base.KwTypeV1;
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
@RequestMapping(value = "/api/v1/posts", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "ApiV1PostController", description = "글 CRUD 컨트롤러")
public class ApiV1PostController {
    private final PostService postService;
    private final Rq rq;
    private final PostVoterService postVoterService;


    public record GetPostsResponseBody(@NonNull PageDto<PostDto> itemPage) {
    }

    @GetMapping(value = "")
    @Operation(summary = "글 다건 조회")
    public RsData<GetPostsResponseBody> getPosts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "") String kw,
            @RequestParam(defaultValue = "ALL") KwTypeV1 kwType
    ) {

        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page - 1, AppConfig.getBasePageSize(), Sort.by(sorts));
        Page<Post> posts = postService.findByKw(kwType, kw, null, true, pageable);

        Page<PostDto> postPage = posts.map(this::postToDto);

        return RsData.of(
                Msg.E200_1_INQUIRY_SUCCEED.getCode(),
                Msg.E200_1_INQUIRY_SUCCEED.getMsg(),
                new GetPostsResponseBody(new PageDto<>(postPage))
        );

    }
    private PostDto postToDto(Post post) {
        PostDto dto = new PostDto(post, rq.getMember());

        return dto;
    }

    @PostMapping("")
    @Operation(summary = "글 등록")
    public RsData<CreatePostDto> createPost(@Valid @RequestBody CreatePostDto createPostDto) {
        Post post = postService.create(rq.getMember(), createPostDto);

        CreatePostDto createdPostDto = new CreatePostDto(post);

        return RsData.of(Msg.E200_0_CREATE_SUCCEED.getCode(),
                Msg.E200_0_CREATE_SUCCEED.getMsg(), createdPostDto);
    }

    @GetMapping("/{id}")
    @Operation(summary = "글 상세 정보")
    public RsData<PostDto> getDetail(@PathVariable("id") Long id) {
        Post post = postService.getPost(id);

        if (!postService.canRead(post))
            throw new GlobalException(CodeMsg.E403_1_NO.getCode(),CodeMsg.E403_1_NO.getMessage());

        PostDto postDto = new PostDto(post, rq.getMember());
        return RsData.of(Msg.E200_1_INQUIRY_SUCCEED.getCode(),
                Msg.E200_1_INQUIRY_SUCCEED.getMsg(), postDto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "글 수정")
    public RsData<PostDto> modify(
            @PathVariable("id") Long id,
            @RequestBody PostDto postDto) {

        if (!postService.haveAuthority(id))
            throw new GlobalException(CodeMsg.E403_1_NO.getCode(),CodeMsg.E403_1_NO.getMessage());

        Post modifyPost = postService.modify(id, postDto);

        PostDto modifyPostDto = new PostDto(modifyPost, rq.getMember());

        return RsData.of(Msg.E200_2_MODIFY_SUCCEED.getCode(),
                Msg.E200_2_MODIFY_SUCCEED.getMsg(), modifyPostDto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "글 삭제")
    public RsData<Empty> delete(@PathVariable("id") Long id) {

        if (!postService.haveAuthority(id))
            throw new GlobalException(CodeMsg.E403_1_NO.getCode(),CodeMsg.E403_1_NO.getMessage());

        postService.delete(id);

        return RsData.of(Msg.E200_3_DELETE_SUCCEED.getCode(),
                Msg.E200_3_DELETE_SUCCEED.getMsg());
    }

    @PostMapping("/{id}/like")
    @Operation(summary = "글 추천")
    public RsData<Void> vote(@PathVariable("id") Long id) {
        Member member = rq.getMember();
        Post post = postService.getPost(id);

        if (!postVoterService.canLike(member, post)) {
            throw new GlobalException(CodeMsg.E400_1_ALREADY_RECOMMENDED.getCode(),CodeMsg.E400_1_ALREADY_RECOMMENDED.getMessage());
        }

        postVoterService.vote(post, member);

        return RsData.of(Msg.E200_4_RECOMMEND_SUCCEED.getCode(),
                Msg.E200_4_RECOMMEND_SUCCEED.getMsg());
    }

    @DeleteMapping("/{id}/like")
    @Operation(summary = "글 추천 취소")
    public RsData<Void> deleteVote(@PathVariable("id") Long id) {
        Member member = rq.getMember();
        Post post = postService.getPost(id);
        if (!postVoterService.canCancelLike(member, post)) {
            throw new GlobalException(CodeMsg.E400_2_NOT_RECOMMENDED_YET.getCode(),CodeMsg.E400_2_NOT_RECOMMENDED_YET.getMessage());
        }

        postVoterService.deleteVote(post, member);

        return RsData.of(Msg.E200_5_CANCEL_RECOMMEND_SUCCEED.getCode(),
                Msg.E200_5_CANCEL_RECOMMEND_SUCCEED.getMsg());
    }

    @PostMapping("/qna")
    @Operation(summary = "1대1 문의 등록")
    public RsData<CreatePostDto> createQna(@Valid @RequestBody CreatePostDto createPostDto) {
        Post post = postService.createQna(rq.getMember(), createPostDto);

        CreatePostDto createdQnaDto = new CreatePostDto(post);

        return RsData.of(Msg.E200_0_CREATE_SUCCEED.getCode(),
                Msg.E200_0_CREATE_SUCCEED.getMsg(), createdQnaDto);
    }

    public record GetMyPostsResponseBody(@NonNull PageDto<PostDto> itemPage) {
    }

    @GetMapping("/myList")
    @Operation(summary = "내 글 목록")
    public RsData<GetMyPostsResponseBody> getMyPosts(
            @RequestParam(defaultValue = "1") int page
    ) {

        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page - 1, AppConfig.getBasePageSize(), Sort.by(sorts));
        Page<Post> posts = postService.getMyPosts(pageable);

        Page<PostDto> postPage = posts.map(this::postMyToDto);

        return RsData.of(
                Msg.E200_1_INQUIRY_SUCCEED.getCode(),
                Msg.E200_1_INQUIRY_SUCCEED.getMsg(),
                new GetMyPostsResponseBody(new PageDto<>(postPage))
        );
    }

    private PostDto postMyToDto(Post post) {
        PostDto dto = new PostDto(post, rq.getMember());

        return dto;
    }

    public record GetQnaResponseBody(@NonNull PageDto<QnaDto> itemPage) {
    }

    @GetMapping("/qna")
    @Operation(summary = "1대1 문의 목록")
    public RsData<GetQnaResponseBody> getMyQna(
            @RequestParam(defaultValue = "1") int page
    ) {

        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page - 1, AppConfig.getBasePageSize(), Sort.by(sorts));
        // PostService에서 모든 QnA 게시물을 가져오기
        Page<Post> qna = postService.getMyQna(pageable);

        Page<QnaDto> qnaPage = qna.map(this::postQnaDto);


        return RsData.of(Msg.E200_1_INQUIRY_SUCCEED.getCode(),
                Msg.E200_1_INQUIRY_SUCCEED.getMsg(),
                new GetQnaResponseBody(new PageDto<>(qnaPage)));

    }

    private QnaDto postQnaDto(Post post) {
        QnaDto dto = new QnaDto(post);

        return dto;
    }

    @GetMapping("/qna/{id}")
    @Operation(summary = "1대1 문의 상세 정보")
    public RsData<QnaDto> getQnaDetail(@PathVariable("id") Long id) {
        Post post = postService.getPost(id);

        if (!postService.canReadQna(post))
            throw new GlobalException(CodeMsg.E403_1_NO.getCode(),CodeMsg.E403_1_NO.getMessage());

        QnaDto qnaDto = new QnaDto(post);
        return RsData.of(Msg.E200_1_INQUIRY_SUCCEED.getCode(),
                Msg.E200_1_INQUIRY_SUCCEED.getMsg(), qnaDto);
    }

    @DeleteMapping("/qna/{id}")
    @Operation(summary = "1대1 문의 삭제")
    public RsData<Empty> deleteQna(@PathVariable("id") Long id) {

        if (!postService.haveAuthority(id))
            throw new GlobalException(CodeMsg.E403_1_NO.getCode(),CodeMsg.E403_1_NO.getMessage());

        postService.delete(id);

        return RsData.of(Msg.E200_3_DELETE_SUCCEED.getCode(),
                Msg.E200_3_DELETE_SUCCEED.getMsg());
    }

    @PatchMapping("/{postId}/report")
    @Operation(summary = "신고하기")
    public RsData<Void> report(@PathVariable("postId") Long id) {

        Post post = postService.getPost(id);

        if (!postService.canReport(rq.getMember(), postService.getPost(id))) {
            throw new GlobalException(CodeMsg.E400_7_ALREADY_REPORT.getCode(),CodeMsg.E400_7_ALREADY_REPORT.getMessage());
        }

        postService.isReported(post);

        return RsData.of(Msg.E200_8_REPORT_SUCCEED.getCode(),
                Msg.E200_8_REPORT_SUCCEED.getMsg());
    }
}