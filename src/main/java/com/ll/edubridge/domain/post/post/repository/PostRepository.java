package com.ll.edubridge.domain.post.post.repository;

import com.ll.edubridge.domain.member.member.entity.Member;
import com.ll.edubridge.domain.post.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long>, CustomPostRepository {
    Page<Post> findByPublishedOrderByIdDesc(boolean published, Pageable pageable);

    Page<Post> findByReport(Pageable pageable, boolean report);

    Page<Post> findByWriterAndPublished(Member member, boolean isPublished, Pageable pageable);

    List<Post> findTop5ByReport(boolean report);

    Page<Post> findByPublished(boolean published, Pageable pageable);

    List<Post> findTop5ByPublished(boolean published);

    List<Post> findByWriterAndReport(Member writer, boolean report);

    Page<Post> findByWriterAndPublishedOrderByIdDesc(Member member, boolean published, Pageable pageable);
}
