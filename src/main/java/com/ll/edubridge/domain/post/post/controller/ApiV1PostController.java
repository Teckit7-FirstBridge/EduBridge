package com.ll.edubridge.domain.post.post.controller;

import com.ll.edubridge.domain.member.member.entity.Member;
import com.ll.edubridge.domain.post.post.dto.PostDto;
import com.ll.edubridge.domain.post.post.dto.QnaDto;
import com.ll.edubridge.domain.post.post.entity.Post;
import com.ll.edubridge.domain.post.post.service.PostService;
import com.ll.edubridge.global.app.AppConfig;
import com.ll.edubridge.global.exceptions.GlobalException;
import com.ll.edubridge.global.rq.Rq;
import com.ll.edubridge.global.rsData.RsData;
import com.ll.edubridge.standard.base.Empty;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/posts", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "ApiV1PostController", description = "글 CRUD 컨트롤러")
public class ApiV1PostController {
    private final PostService postService;
    private final Rq rq;

    @Getter
    public static class GetPostsResponseBody {
        @NonNull
        private final List<PostDto> items;

        public GetPostsResponseBody(Page<Post> page, Member currentUser) {
            this.items = page.getContent().stream()
                    .map(post -> new PostDto(post, currentUser))
                    .toList();
        }
    }

    @GetMapping("")
    @Operation(summary = "글 리스트")
    public RsData<GetPostsResponseBody> getPosts(
            @RequestParam(value = "page", defaultValue = "1") int page
    ) {
        int pageSize = AppConfig.getBasePageSize();
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Direction.DESC, "id"));

        Page<Post> postPage = postService.findByPublished(true, pageable);
        GetPostsResponseBody responseBody = new GetPostsResponseBody(postPage,rq.getMember());

        return RsData.of(
                "200-1",
                "성공",
                responseBody
        );
    }

    @PostMapping("")
    @Operation(summary = "글 등록")
    public RsData<PostDto> createPost(@RequestBody PostDto postDto) {
        Post post = postService.create(rq.getMember(), postDto);

        PostDto createdPostDto = new PostDto(post,rq.getMember());

        return RsData.of("200-0", "등록 성공", createdPostDto);
    }

    @GetMapping("/{id}")
    @Operation(summary = "글 상세 정보")
    public RsData<PostDto> getDetail(@PathVariable("id") Long id) {
        Post post = postService.getPost(id);

        if(!postService.canRead(post))
            throw new GlobalException("403-1", "권한이 없습니다.");

        PostDto postDto = new PostDto(post, rq.getMember());
        return RsData.of("200-1", "성공", postDto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "글 수정")
    public RsData<PostDto> modify(
            @PathVariable("id") Long id,
            @RequestBody PostDto postDto) {

        if(!postService.haveAuthority(id))
            throw new GlobalException("403-1", "권한이 없습니다.");

        Post modifyPost = postService.modify(id, postDto);

        PostDto modifyPostDto = new PostDto(modifyPost, rq.getMember());

        return RsData.of("200-2", "수정 성공", modifyPostDto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "글 삭제")
    public RsData<Empty> delete(@PathVariable("id") Long id) {

        if(!postService.haveAuthority(id))
            throw new GlobalException("403-1", "권한이 없습니다.");

        postService.delete(id);

        return RsData.of("200-3", "삭제 성공");
    }

    @PostMapping("/{id}/like")
    @Operation(summary = "글 추천")
    public RsData<Void> vote(@PathVariable("id") Long id) {
        Member member = rq.getMember();

        if (!postService.canLike(member, postService.getPost(id))) {
            throw new GlobalException("400-1", "이미 추천하셨습니다.");
        }

        postService.vote(id, member);

        return RsData.of("200-4", "추천 성공");
    }

    @DeleteMapping("/{id}/like")
    @Operation(summary = "글 추천 취소")
    public RsData<Void> deleteVote(@PathVariable("id") Long id) {
        Member member = rq.getMember();

        if (!postService.canCancelLike(member, postService.getPost(id))) {
            throw new GlobalException("400-2", "추천을 하지 않았습니다.");
        }

        postService.deleteVote(id, member);

        return RsData.of("200-5", "추천 취소 성공");
    }

    @PostMapping("/qna")
    @Operation(summary = "1대1 문의 등록")
    public RsData<QnaDto> createQna(@RequestBody QnaDto qnaDto) {
        Post post = postService.createQna(rq.getMember(), qnaDto);

        QnaDto createdQnaDto = new QnaDto(post);

        return RsData.of("200-0", "등록 성공", createdQnaDto);
    }

    @GetMapping("/qna")
    @Operation(summary = "1대1 문의 목록")
    public RsData<List<QnaDto>> getMyQna() {
        // PostService에서 모든 QnA 게시물을 가져오기
        List<Post> qnaPosts = postService.getMyQna();

        // Post 객체를 QnaDto로 변환
        List<QnaDto> qnaDtoList = qnaPosts.stream()
                .map(QnaDto::new)
                .collect(Collectors.toList());

        return RsData.of("200-1", "조회 성공", qnaDtoList);
    }

    @GetMapping("/qna/{id}")
    @Operation(summary = "1대1 문의 상세 정보")
    public RsData<QnaDto> getQnaDetail(@PathVariable("id") Long id) {
        Post post = postService.getPost(id);

        if(!postService.canRead(post))
            throw new GlobalException("403-1", "권한이 없습니다.");

        QnaDto qnaDto = new QnaDto(post);
        return RsData.of("200-1", "성공", qnaDto);
    }

    @DeleteMapping("/qna/{id}")
    @Operation(summary = "1대1 문의 삭제")
    public RsData<Empty> deleteQna(@PathVariable("id") Long id) {

        if(!postService.haveAuthority(id))
            throw new GlobalException("403-1", "권한이 없습니다.");

        postService.delete(id);

        return RsData.of("200-3", "삭제 성공");
    }

}
