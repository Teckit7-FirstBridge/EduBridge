package com.ll.edubridge.domain.post.post.service;

import com.ll.edubridge.domain.member.member.entity.Member;
import com.ll.edubridge.domain.member.member.service.MemberService;
import com.ll.edubridge.domain.post.post.entity.Post;
import com.ll.edubridge.domain.post.post.repository.PostRepository;
import com.ll.edubridge.global.exceptions.GlobalException;
import com.ll.edubridge.global.rq.Rq;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
    public void modify(Post post, String title, String content) {
        post.setTitle(title);
        post.setContent(content);
        postRepository.save(post);
    }

    @Transactional
    public void delete(Post post) {
        postRepository.delete(post);
    }

    @Transactional
    public void save(Post post) {
        postRepository.save(post);
    }

    @Transactional
    public Page<Post> findAll(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    @Transactional
    public void vote(Long id, Member member) {
        Post post = this.getPost(id);
        post.getVoter().add(member);
        postRepository.save(post);
    }

    @Transactional
    public void deleteVote(Long id, Member member) {
        Post post = this.getPost(id);
        post.getVoter().remove(member);
        postRepository.save(post);
    }

    private Post getPost(Long id) {
        Optional<Post> comment = this.postRepository.findById(id);
        if (comment.isPresent()) {
            return comment.get();
        } else {
            throw new GlobalException("404-1", "post를 찾을 수 없습니다.");
        }
    }


    private Optional<Post> findById(Long id) {
        return postRepository.findById(id);
    }

    @Transactional
    public void isReported(Post post) {
        Member member = post.getWriter();

        post.setReport(true);

        memberService.isReported(member);

        postRepository.save(post);
    }

    public Page<Post> getReport(Pageable pageable) {
        return postRepository.findByReport(pageable, true);
    }


    public boolean canLike(Member member, Post post) {
        if (member == null) return false;
        if (post == null) return false;

        return !post.getVoter().contains(member);
    }

    public boolean canCancelLike(Member member, Post post) {
        if (member == null) return false;
        if (post == null) return false;

        return post.getVoter().contains(member);
    }

    public boolean haveAuthority(Post post) {
        Member member = rq.getMember();

        if (member == null) return false;

        if (rq.isAdmin()) return true;

        return post.getWriter().equals(member);
    }

    public boolean canRead(Post post){
        Member member=rq.getMember();

        if(rq.isAdmin()) return true;

        if(post.isPublished()) return false;

        return member.equals(post.getWriter());
    }

    public Page<Post> findByPublished(boolean published, Pageable pageable) {
        return postRepository.findByPublishedOrderByIdDesc(published);
    }
}
