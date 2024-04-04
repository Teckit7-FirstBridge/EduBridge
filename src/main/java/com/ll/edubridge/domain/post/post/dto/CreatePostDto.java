package com.ll.edubridge.domain.post.post.dto;

import com.ll.edubridge.domain.post.post.entity.Post;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import static lombok.AccessLevel.PROTECTED;

@NoArgsConstructor(access = PROTECTED)
@Getter
public class CreatePostDto {
    @NonNull
    @Size(max = 200)
    private String title;
    @NonNull
    private String body;

    public CreatePostDto(Post post) {
        this.title = post.getTitle();
        this.body = post.getContent();
    }
}
