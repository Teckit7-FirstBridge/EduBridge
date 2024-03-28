package com.ll.edubridge.domain.PostVoter.repository;

import com.ll.edubridge.domain.PostVoter.entity.PostVoter;
import com.ll.edubridge.domain.member.member.entity.Member;
import com.ll.edubridge.domain.post.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostVoterRepository extends JpaRepository<PostVoter,Long> {
    public void deletePostVoterByPostAndMember(Post post, Member member);
}
