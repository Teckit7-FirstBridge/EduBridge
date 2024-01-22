package com.ll.edubridge.domain.post.post.service;

import com.ll.edubridge.domain.member.member.entity.Member;
import com.ll.edubridge.domain.member.member.repository.MemberRepository;
import com.ll.edubridge.domain.member.member.service.MemberService;
import com.ll.edubridge.domain.post.post.entity.Post;
import com.ll.edubridge.domain.post.post.repository.PostRepository;
import com.ll.edubridge.global.rq.Rq;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;

    private final MemberService memberService;

    private final Rq rq;

    @Transactional
    public void create(Member author, String title, String content) {
        Post post = Post.builder()
                .writer(author)
                .title(title)
                .content(content)
                .build();

        postRepository.save(post);
    }

    @Transactional
    public void createQna(Member author, String title, String content) {
        Post post = Post.builder()
                .writer(author)
                .title(title)
                .content(content)
                .published(false)
                .build();

        postRepository.save(post);
    }

    @Transactional
    public void modify(Post post, String title, String content){
        post.setTitle(title);
        post.setContent(content);
        postRepository.save(post);
    }

    @Transactional
    public void delete(Post post){
        postRepository.delete(post);
    }

    @Transactional
    public void save(Post post){
        postRepository.save(post);
    }

    @Transactional
    public Page<Post> findAll(Pageable pageable){
        return postRepository.findAll(pageable);
    }

    @Transactional
    public void vote(Post post, Member member){
        post.getVoter().add(member);
        postRepository.save(post);
    }

    @Transactional
    public void deleteVote(Post post, Member member){
        post.getVoter().remove(member);
        postRepository.save(post);
    }

    @Transactional
    public void isReported(Post post){
        Member member=post.getWriter();

        post.setReport(true);

        memberService.isReported(member);

        postRepository.save(post);
    }

    public Page<Post> getReport(Pageable pageable){
        return postRepository.findByReport(pageable, true);
    }


    public boolean canLike(Member member, Post post){
        if(member==null) {
            return false;
        }
        return !post.getVoter().contains(member);
    }

    public boolean canCancelLike(Member member, Post post){
        if(member==null){
            return false;
        }
        return post.getVoter().contains(member);
    }

    public boolean haveAuthority(Post post){
        Member member=rq.getMember();

        if(member==null) return false;

        return post.getWriter().equals(member);
    }

    public List<Post> findByPublished(boolean published) {
        return postRepository.findByPublishedOrderByIdDesc(published);
    }
}
