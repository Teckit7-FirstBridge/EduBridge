package com.ll.edubridge.domain.post.post.repository;

import com.ll.edubridge.domain.post.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findByPublishedOrderByIdDesc(boolean published);

    Page<Post> findByReport(Pageable pageable, boolean report);
}
