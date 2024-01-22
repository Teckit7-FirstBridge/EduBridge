package com.ll.edubridge.domain.post.post.service;

import com.ll.edubridge.domain.member.member.entity.Member;
import com.ll.edubridge.domain.post.post.entity.Post;
import com.ll.edubridge.domain.post.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {
    private final PostRepository postRepository;

    @Transactional
    public void create(Member author, String title, String content) {
        Post post = Post.builder()
                .writer(author)
                .title(title)
                .content(content)
                .build();

        postRepository.save(post);
    }

    public List<Post> findByPublished(boolean published) {
        return postRepository.findByPublishedOrderByIdDesc(published);
    }
}
