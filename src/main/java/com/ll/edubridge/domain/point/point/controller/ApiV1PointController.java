package com.ll.edubridge.domain.point.point.controller;

import com.ll.edubridge.domain.point.point.dto.AttendDto;
import com.ll.edubridge.domain.point.point.dto.PointDto;
import com.ll.edubridge.domain.point.point.entity.Point;
import com.ll.edubridge.domain.point.point.service.PointService;
import com.ll.edubridge.global.msg.Msg;
import com.ll.edubridge.global.rq.Rq;
import com.ll.edubridge.global.rsData.RsData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/point", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "ApiV1PointController", description = "포인트 조회용 컨트롤러")
public class ApiV1PointController {
    private final PointService pointService;
    private final Rq rq;

    @GetMapping("/{memberId}")
    @Operation(summary = "포인트 목록")
    public RsData<List<PointDto>> getPoints(@PathVariable("memberId") Long memberId) {
        List<Point> points = pointService.findByOwnerId(memberId);

        List<PointDto> pointDtoList = points.stream()
                .map(PointDto::new)
                .toList();

        return RsData.of(Msg.E200_1_INQUIRY_SUCCEED.getCode(),
                Msg.E200_1_INQUIRY_SUCCEED.getMsg(), pointDtoList);
    }

    @GetMapping("/attend")
    @Operation(summary = "출석 체크 목록")
    public RsData<List<AttendDto>> getAttend(){
        List<Point> points = pointService.findByOwnerIdAndContent(rq.getMember().getId());

        List<AttendDto> attendDtoList = points.stream()
                .map(AttendDto::new)
                .toList();

        return RsData.of(Msg.E200_1_INQUIRY_SUCCEED.getCode(),
                Msg.E200_1_INQUIRY_SUCCEED.getMsg(), attendDtoList);
    }
}
