package com.ll.edubridge.domain.post.post.dto;

import com.ll.edubridge.domain.post.post.entity.Post;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.springframework.lang.NonNull;


@Getter
public class CreateDto {

    @NonNull
    @Size(max = 200)
    private String title;
    @NonNull
    private String body;

    public CreateDto(Post post) {
        this.title = post.getTitle();
        this.body = post.getContent();
    }

}
