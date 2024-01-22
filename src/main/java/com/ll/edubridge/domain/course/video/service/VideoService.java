package com.ll.edubridge.domain.course.video.service;

import com.ll.edubridge.domain.course.course.entity.Course;
import com.ll.edubridge.domain.course.summaryNote.entity.SummaryNote;
import com.ll.edubridge.domain.course.video.entity.Video;
import com.ll.edubridge.domain.course.video.repository.VideoRepository;
import com.ll.edubridge.domain.member.member.entity.Member;
import com.ll.edubridge.global.rq.Rq;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VideoService {
    private final Rq rq;
    private final VideoRepository videoRepository;

    @Transactional
    public void create(String url, String overView, Course course, List<SummaryNote> summaryNotes){
        Video video = Video.builder()
                .url(url)
                .overView(overView)
                .course(course)
                .summaryNotes(summaryNotes)
                .build();
        videoRepository.save(video);
    }

    @Transactional
    public void modify(){

    }

    @Transactional
    public void delete(){

    }

    @Transactional
    public Optional<Video> findByID(long id){
        return Optional.ofNullable(videoRepository.findById(id));
    }

    @Transactional
    public void save(Video video){
        videoRepository.save(video);
    }

    @Transactional
    public Page<Video> findAll(int page, long courseId) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.asc("id"));
        Pageable pageable = PageRequest.of(page, 5, Sort.by(sorts));
        return this.videoRepository.findByCourseId(pageable, courseId);
    }

    @Transactional
    public boolean haveAuthority(){
        Member member = rq.getMember();
        if(member.getUsername().equals("admin")) return true;
        return false;
    }
}
