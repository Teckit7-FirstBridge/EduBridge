package com.ll.edubridge.domain.course.video.service;

import com.ll.edubridge.domain.course.video.dto.CreateVideoDto;
import com.ll.edubridge.domain.course.video.dto.VideoDto;
import com.ll.edubridge.domain.course.video.entity.Video;
import com.ll.edubridge.domain.course.video.repository.VideoRepository;
import com.ll.edubridge.domain.member.member.entity.Member;
import com.ll.edubridge.global.exceptions.GlobalException;
import com.ll.edubridge.global.rq.Rq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VideoService {
    private final Rq rq;
    private final VideoRepository videoRepository;
    // private final CourseService courseService;

    @Transactional
    public Video create(CreateVideoDto createVideoDto) {
        Video video = Video.builder()
                .url(createVideoDto.getUrl())
                .overView(createVideoDto.getOverView())
                .course(createVideoDto.getCourse())
                .summaryNotes(createVideoDto.getSummaryNotes())
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
    public Video findById(Long courseId, Long id) {
        return videoRepository.findById(id);
    }

    @Transactional
    public List<Video> findAll(Long courseId) {
        List<Video> videoList = videoRepository.findByCourseId(courseId);
        videoList.sort((v1, v2) -> v1.getId().compareTo(v2.getId()));
        return videoList;
    }

    @Transactional
    public void save(Video video) {
        videoRepository.save(video);
    }

    @Transactional
    public boolean haveAuthority(Long courseId) {
        Member member = rq.getMember();

        // Course course = courseService.findById(courseId);

        if (member == null) return false;

        if (rq.isAdmin()) return true;

        return true; // Course에 owner 필드 생성 전이라 임시 작성
        // return course.getOwner().equals(member);
    }
}
