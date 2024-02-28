package com.ll.edubridge.domain.post.post.service;

import com.ll.edubridge.domain.member.member.entity.Member;
import com.ll.edubridge.domain.member.member.repository.MemberRepository;
import com.ll.edubridge.domain.member.member.service.MemberService;
import com.ll.edubridge.domain.post.post.dto.CreatePostDto;
import com.ll.edubridge.domain.post.post.dto.PostDto;
import com.ll.edubridge.domain.post.post.entity.Post;
import com.ll.edubridge.domain.post.post.repository.PostRepository;
import com.ll.edubridge.global.exceptions.CodeMsg;
import com.ll.edubridge.global.exceptions.GlobalException;
import com.ll.edubridge.global.rq.Rq;
import com.ll.edubridge.standard.base.KwTypeV1;
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
    private final MemberRepository memberRepository;

    @Transactional
    public Post create(Member member, CreatePostDto createPostDto) {
        Post post = Post.builder()
                .writer(member)
                .title(createPostDto.getTitle())
                .published(true)
                .content(createPostDto.getBody())
                .build();

        return postRepository.save(post);
    }

    @Transactional
    public Post createQna(Member member, CreatePostDto createPostDto) {
        Post post = Post.builder()
                .writer(member)
                .title(createPostDto.getTitle())
                .content(createPostDto.getBody())
                .published(false)
                .build();

        return postRepository.save(post);
    }

    @Transactional
    public Post modify(Long id, PostDto postDto) {
        Post post = this.getPost(id);

        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getBody());

        return postRepository.save(post);
    }

    @Transactional
    public void delete(Long id) {
        Post post = this.getPost(id);
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

    public Post getPost(Long id) {
        Optional<Post> comment = this.findById(id);
        if (comment.isPresent()) {
            return comment.get();
        } else {
            throw new GlobalException(CodeMsg.E404_1_DATA_NOT_FIND.getCode(),CodeMsg.E404_1_DATA_NOT_FIND.getMessage());
        }
    }


    public Optional<Post> findById(Long id) {
        return postRepository.findById(id);
    }

    @Transactional
    public void isReported(Post post) {
        Member member = post.getWriter();

        post.setReport(true);

        memberService.isReported(member);

        postRepository.save(post);
        memberRepository.save(member);
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

    public boolean canReport(Member member, Post post) {
        if (member == null) return false;
        if (post == null) return false;

        return !post.isReport();
    }

    public boolean haveAuthority(Long id) {
        Member member = rq.getMember();

        Post post = this.getPost(id);

        if (member == null) return false;

        if (rq.isAdmin()) return true;

        return post.getWriter().equals(member);
    }

    public boolean canRead(Post post) {
        Member member = rq.getMember();

        if(!post.isPublished()&&member.equals(post.getWriter())) return false;

        return true;
    }

    public boolean canReadQna(Post post) {
        Member member = rq.getMember();

        if(rq.isAdmin()||member.getId().equals(post.getWriter().getId())) return true;

        return false;
    }

    public Page<Post> findByPublished(boolean published, Pageable pageable) {
        return postRepository.findByPublishedOrderByIdDesc(published, pageable);
    }

    public Page<Post> getMyPosts(Pageable pageable) {
        Member member = rq.getMember();

        return postRepository.findByWriterAndPublishedOrderByIdDesc(member,true, pageable);
    }



    public Page<Post> getMyQna(Pageable pageable) {
        Member member = rq.getMember();

        return postRepository.findByWriterAndPublishedOrderByIdDesc(member, false, pageable);
    }


    public Page<Post> findByKw(KwTypeV1 kwType, String kw, Member author, Boolean published, Pageable pageable) {
        return postRepository.findByKw(kwType, kw, author, published, pageable);
    }

    public List<Post> reportedPosts() {
        return postRepository.findTop5ByReport(true);
    }

    public boolean hasNotReported(Member member) {
        return postRepository.findByWriterAndReport(member, true).isEmpty();
    }

    public boolean canCancelReport( Post post) {
        if (rq.isAdmin() && post.isReport()){
            return true;
        }

        return false;
    }

    @Transactional
    public void deleteReport(Long id) {
        Post post = this.getPost(id);

        post.setReport(false);

        postRepository.save(post);
    }

    public Page<Post> findAllQna(Pageable pageable) {
        return postRepository.findByPublished(false, pageable);
    }

    public List<Post> recentQna() {
        return postRepository.findTop5ByPublished(false);
    }

}