package com.ll.edubridge.domain.post.post.repository;

import com.ll.edubridge.domain.member.member.entity.Member;
import com.ll.edubridge.domain.post.post.entity.Post;
import com.ll.edubridge.standard.base.KwTypeV1;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomPostRepository {
    Page<Post> findByKw(KwTypeV1 kwType, String kw, Member author, Boolean published, Pageable pageable);
}
