package com.ll.edubridge.domain.post.postVoter.service;

import com.ll.edubridge.domain.post.postVoter.entity.PostVoter;
import com.ll.edubridge.domain.post.postVoter.repository.PostVoterRepository;
import com.ll.edubridge.domain.member.member.entity.Member;
import com.ll.edubridge.domain.post.post.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostVoterService {
    private final PostVoterRepository postVoterRepository;

    @Transactional
    public void vote(Post post, Member member) {
        PostVoter postVoter = new PostVoter(post,member);
        postVoterRepository.save(postVoter);
    }

    @Transactional
    public void deleteVote(Post post, Member member) {
        postVoterRepository.deletePostVoterByPostAndMember(post,member);
    }

    public boolean canLike(Member member, Post post) {
        if (member == null) return false;
        if (post == null) return false;

        return !post.getPostVoters().contains(new PostVoter(post,member));
    }

    public boolean canCancelLike(Member member, Post post) {
        if (member == null) return false;
        if (post == null) return false;

        return post.getPostVoters().contains(new PostVoter(post,member));
    }
}
