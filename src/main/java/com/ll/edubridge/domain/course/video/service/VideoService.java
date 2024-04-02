package com.ll.edubridge.domain.course.video.service;

import com.ll.edubridge.domain.course.course.entity.Course;
import com.ll.edubridge.domain.course.course.service.CourseService;
import com.ll.edubridge.domain.course.video.dto.CreateVideoDto;
import com.ll.edubridge.domain.course.video.dto.VideoDto;
import com.ll.edubridge.domain.course.video.entity.Video;
import com.ll.edubridge.domain.course.video.repository.VideoRepository;
import com.ll.edubridge.domain.member.member.entity.Member;
import com.ll.edubridge.global.exceptions.CodeMsg;
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
    private final CourseService courseService;

    @Transactional
    public Video create(CreateVideoDto createVideoDto) {

        String videoID = this.getVideoId(createVideoDto.getUrl());

        Video video = Video.builder()
                .url("https://youtu.be/"+videoID)
                .overView(createVideoDto.getOverView())
                .course(courseService.getCourse(createVideoDto.getCourseId()))
                .imgUrl("https://img.youtube.com/vi/"+videoID+"/0.jpg")
                .keywords(createVideoDto.getKeywords())
                .title(createVideoDto.getTitle())
                .build();
        return videoRepository.save(video);
    }

    private String getVideoId(String url) {
        if( !(url.startsWith("https://www.youtube.com") || url.startsWith("https://youtu.be")) ) {
            throw new GlobalException(CodeMsg.E400_13_WRONG_URL.getCode(), CodeMsg.E400_13_WRONG_URL.getMessage());
        }

        if(url.startsWith("https://www.youtube.com/shorts")) {
            throw new GlobalException(CodeMsg.E400_13_WRONG_URL.getCode(), CodeMsg.E400_13_WRONG_URL.getMessage());
        }

        String videoID = url.substring(8);
        if(videoID.startsWith("youtu.be/")) {
            videoID = videoID.substring(9, 9+11);
            return videoID;
        } else if(videoID.startsWith("www.youtube.com/")) {
            videoID = videoID.substring(24, 24+11);
            return videoID;
        } else {
            throw new GlobalException(CodeMsg.E400_13_WRONG_URL.getCode(), CodeMsg.E400_13_WRONG_URL.getMessage());
        }
    }

    @Transactional
    public Video modify(Long id, VideoDto videoDto) {
        Video video = this.getVideo(id);
        String videoID = this.getVideoId(videoDto.getUrl());

        video.setUrl("https://youtu.be/"+videoID);
        video.setOverView(videoDto.getOverView());
        video.setImgUrl("https://img.youtube.com/vi/"+videoID+"/0.jpg");
        video.setKeywords(videoDto.getKeywords());
        video.setTitle(videoDto.getTitle());

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
            throw new GlobalException(CodeMsg.E404_1_DATA_NOT_FIND.getCode(), CodeMsg.E404_1_DATA_NOT_FIND.getMessage());
        }
    }

    @Transactional
    public Optional<Video> findById(Long id) {
        return Optional.ofNullable(videoRepository.findById(id));
    }

    @Transactional

    public Video findByCourseIdAndId(Long courseId, Long id) {
        return videoRepository.findByCourseIdAndId(courseId, id);
    }

    @Transactional
    public List<Video> findByCourseId(Long courseId) {
        return videoRepository.findByCourseIdOrderByIdAsc(courseId);
    }

    @Transactional
    public void save(Video video) {
        videoRepository.save(video);
    }

    @Transactional
    public boolean haveAuthority(Course course) {
        Member member = rq.getMember();
        return member.getId().equals(course.getWriter().getId());
    }
}
