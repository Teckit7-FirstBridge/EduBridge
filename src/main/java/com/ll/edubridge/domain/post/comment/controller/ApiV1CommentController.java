package com.ll.edubridge.domain.post.comment.controller;

import com.ll.edubridge.domain.post.post.dto.PostDto;
import com.ll.edubridge.domain.post.post.entity.Post;
import com.ll.edubridge.domain.post.post.service.PostService;
import com.ll.edubridge.global.rsData.RsData;
import io.swagger.v3.oas.annotations.Operation;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping
public class ApiV1CommentController {
    private final PostService postService;

    @Getter
    public static class GetPostsResponseBody {
        @NonNull
        private final List<PostDto> items;

        public GetPostsResponseBody(Page<Post> page) {
            this.items = page.getContent().stream()
                    .map(PostDto::new)
                    .toList();
        }
    }

    @GetMapping("")
    @Operation(summary = "글 리스트")
    public RsData<GetPostsResponseBody> getPosts(
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<Post> page = postService.findByPublished(true, pageable);
        GetPostsResponseBody responseBody = new GetPostsResponseBody(page);

        return RsData.of(
                "200-1",
                "성공",
                responseBody
        );
    }
}
