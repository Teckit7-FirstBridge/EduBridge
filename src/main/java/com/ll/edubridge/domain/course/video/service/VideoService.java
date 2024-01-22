package com.ll.edubridge.domain.course.video.service;

import com.ll.edubridge.domain.course.video.dto.VideoDto;
import com.ll.edubridge.domain.course.video.entity.Video;
import com.ll.edubridge.domain.course.video.repository.VideoRepository;
import com.ll.edubridge.domain.member.member.entity.Member;
import com.ll.edubridge.global.exceptions.GlobalException;
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
    public Video create(VideoDto videoDto) {
        Video video = Video.builder()
                .url(videoDto.getUrl())
                .overView(videoDto.getOverView())
                .course(videoDto.getCourse())
                .summaryNotes(videoDto.getSummaryNotes())
                .build();
        return videoRepository.save(video);
    }

    @Transactional
    public Video modify(Long id, VideoDto videoDto) {
        Video video = this.getVideo(id);

        video.setUrl(videoDto.getUrl());
        video.setOverView(videoDto.getOverView());

        return videoRepository.save(video);
    }

    @Transactional
    public void delete(Long id) {
        Video video = this.getVideo(id);
        videoRepository.delete(video);
    }

    @Transactional
    public Video getVideo(Long id) {
        Optional<Video> video = this.findById(id);
        if (video.isPresent()) {
            return video.get();
        } else {
            throw new GlobalException("404-1", "해당 영상을 찾을 수 없습니다.");
        }
    }

    @Transactional
    public Optional<Video> findById(Long id) {
        return Optional.ofNullable(videoRepository.findById(id));
    }

    @Transactional
    public Page<Video> findAll(int page, Long courseId) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.asc("id"));
        Pageable pageable = PageRequest.of(page, 5, Sort.by(sorts));
        return this.videoRepository.findByCourseId(pageable, courseId);
    }

    @Transactional
    public void save(Video video) {
        videoRepository.save(video);
    }

    @Transactional
    public boolean haveAuthority() {
        Member member = rq.getMember();
        if (member.getUsername().equals("admin")) return true;
        return false;
    }
}
