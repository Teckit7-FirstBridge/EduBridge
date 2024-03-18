package com.ll.edubridge.domain.course.roadmap.controller;

import com.ll.edubridge.domain.course.course.dto.NumDto;
import com.ll.edubridge.domain.course.course.entity.Course;
import com.ll.edubridge.domain.course.course.service.CourseService;
import com.ll.edubridge.domain.course.roadmap.dto.CreateRoadmapDto;
import com.ll.edubridge.domain.course.roadmap.dto.RoadmapDto;
import com.ll.edubridge.domain.course.roadmap.entity.Roadmap;
import com.ll.edubridge.domain.course.roadmap.service.RoadmapService;
import com.ll.edubridge.global.app.AppConfig;
import com.ll.edubridge.global.msg.Msg;
import com.ll.edubridge.global.rq.Rq;
import com.ll.edubridge.global.rsData.RsData;
import com.ll.edubridge.standard.base.Empty;
import com.ll.edubridge.standard.base.KwTypeCourse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/roadmap", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "ApiV1RoadmapController", description = "로드맵 CRUD 컨트롤러")
public class ApiV1RoadmapController {
    private final RoadmapService roadmapService;
    private final Rq rq;
    private final CourseService courseService;

    @Getter
    public class GetRoadmapsResponsebody {
        @NonNull
        private final List<RoadmapDto> items;

        public GetRoadmapsResponsebody(Page<Roadmap> page) {
            this.items = page.getContent().stream()
                    .map(roadmap -> new RoadmapDto(roadmap, rq.getMember()))
                    .toList();
        }
    }

    @GetMapping(value = "")
    @Operation(summary = "로드맵 다건 조회")
    public RsData<ApiV1RoadmapController.GetRoadmapsResponsebody> getRoadmaps(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "") String kw,
            @RequestParam(defaultValue = "ALL") KwTypeCourse kwType
    ) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page - 1, AppConfig.getBasePageSize(), Sort.by(sorts));

        Page<Roadmap> roadmapPage = roadmapService.findByKw(kwType, kw, pageable);

        ApiV1RoadmapController.GetRoadmapsResponsebody responseBody = new ApiV1RoadmapController.GetRoadmapsResponsebody(roadmapPage);

        return RsData.of(
                Msg.E200_1_INQUIRY_SUCCEED.getCode(),
                Msg.E200_1_INQUIRY_SUCCEED.getMsg(),
                responseBody
        );
    }

    @GetMapping(value = "/myRoadmap")
    @Operation(summary = "내가 등록한 로드맵 다건 조회")
    public RsData<List<RoadmapDto>> getMyRoadmaps() {

        List<Roadmap> roadmapList = roadmapService.getMyRoadmaps(rq.getMember());

        List<RoadmapDto> roadmapDtoList = roadmapList.stream().map(RoadmapDto::new).toList();

        return RsData.of(
                Msg.E200_1_INQUIRY_SUCCEED.getCode(),
                Msg.E200_1_INQUIRY_SUCCEED.getMsg(),
                roadmapDtoList
        );
    }

    @GetMapping("/{roadmapId}")
    @Operation(summary = "로드맵 아이디로 상세 조회")
    public RsData<RoadmapDto> getRoadmapById(@PathVariable("roadmapId") Long roadmapId) {
        Roadmap roadmap = roadmapService.getRoadmap(roadmapId);
        RoadmapDto roadmapDto = new RoadmapDto(roadmap, rq.getMember());

        return RsData.of(Msg.E200_1_INQUIRY_SUCCEED.getCode(),
                Msg.E200_1_INQUIRY_SUCCEED.getMsg(), roadmapDto);
    }

    @GetMapping("/{courseId}")
    @Operation(summary = "로드맵 강좌로 상세 조회")
    public RsData<RoadmapDto> getRoadmapByCourse(@PathVariable("courseId") Long courseId) {
        Course course = courseService.getCourse(courseId);
        Roadmap roadmap = roadmapService.getCourseRoadmap(course);
        RoadmapDto roadmapDto = new RoadmapDto(roadmap, rq.getMember());

        return RsData.of(Msg.E200_1_INQUIRY_SUCCEED.getCode(),
                Msg.E200_1_INQUIRY_SUCCEED.getMsg(), roadmapDto);
    }

    @PostMapping("/roadmaps")
    @Operation(summary = "로드맵 생성")
    public RsData<CreateRoadmapDto> createRoadmap(@Valid @RequestBody CreateRoadmapDto createRoadmapDto) {

        Roadmap roadmap = roadmapService.create(createRoadmapDto);

        CreateRoadmapDto createdRoadmapDto = new CreateRoadmapDto(roadmap);

        return RsData.of(Msg.E200_0_CREATE_SUCCEED.getCode(),
                Msg.E200_0_CREATE_SUCCEED.getMsg(),
                createdRoadmapDto);
    }

    @PutMapping("/roadmaps/{roadmapId}/{courseId}")
    @Operation(summary = "로드맵에 강좌 추가")
    public RsData<Empty> addCourse(
            @PathVariable("roadmapId") Long roadmapId,
            @PathVariable("courseId") Long courseId) {

        Course course = courseService.getCourse(courseId);
        roadmapService.addCourse(roadmapId, course);

        return RsData.of(Msg.E200_2_MODIFY_SUCCEED.getCode(),
                Msg.E200_2_MODIFY_SUCCEED.getMsg());
    }

    @PutMapping("/roadmaps/{id}")
    @Operation(summary = "로드맵 수정")
    public RsData<RoadmapDto> modifyRoadmap(
            @PathVariable("id") Long id,
            @RequestBody RoadmapDto roadmapDto) {

        Roadmap modifyRoadmap = roadmapService.modify(id, roadmapDto);

        RoadmapDto modifyCourseDto = new RoadmapDto(modifyRoadmap, rq.getMember());

        return RsData.of(Msg.E200_2_MODIFY_SUCCEED.getCode(),
                Msg.E200_2_MODIFY_SUCCEED.getMsg(), modifyCourseDto);
    }

    @PutMapping("/changeNum/{courseId}")
    @Operation(summary = "로드맵 순서 변경")
    public RsData<Empty> changeRoadmapNum(@PathVariable Long courseId,
                                          @RequestBody NumDto numDto){

        courseService.changeRoadmapNum(courseId, numDto);


        return RsData.of(Msg.E200_2_MODIFY_SUCCEED.getCode(),
                Msg.E200_2_MODIFY_SUCCEED.getMsg());
    }


    @DeleteMapping("/roadmaps/{id}")
    @Operation(summary = "로드맵 삭제")
    public RsData<Empty> deleteCourse(@PathVariable("id") Long id) {

        roadmapService.delete(id);

        return RsData.of(Msg.E200_3_DELETE_SUCCEED.getCode(),
                Msg.E200_3_DELETE_SUCCEED.getMsg());
    }
}
