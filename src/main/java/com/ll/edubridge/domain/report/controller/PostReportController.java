package com.ll.edubridge.domain.report.controller;

import com.ll.edubridge.domain.report.dto.CreatePostReportDto;
import com.ll.edubridge.domain.report.entity.PostReport;
import com.ll.edubridge.domain.report.service.PostReportService;
import com.ll.edubridge.global.exceptions.CodeMsg;
import com.ll.edubridge.global.exceptions.GlobalException;
import com.ll.edubridge.global.msg.Msg;
import com.ll.edubridge.global.rq.Rq;
import com.ll.edubridge.global.rsData.RsData;
import com.ll.edubridge.standard.base.Empty;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value ="/api/v1/postReport", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "ApiV1CourseReportController", description = "글 신고 컨트롤러")
public class PostReportController{
    private final PostReportService postReportService;
    private final Rq rq;

    @PostMapping("/{postId}") // postId 을 쓰기 위해서 주소로 넣는다. --> PathVariable 필요
    @Operation(summary = "글 신고") // @RequestBody : 컨트롤러에서 클라이언트가 요청 본문에 보낸 데이터를 처리하는데 사용
    public RsData<CreatePostReportDto> createPostReportDto(@PathVariable("postId") Long postId, @Valid @RequestBody CreatePostReportDto createPostReportDto){
        PostReport postReport = postReportService.create(rq.getMember(), createPostReportDto,postId);

        CreatePostReportDto reportDto = new CreatePostReportDto(postReport);

        return RsData.of(Msg.E200_8_REPORT_SUCCEED.getCode(),
                Msg.E200_8_REPORT_SUCCEED.getMsg(), reportDto);

    }


    @DeleteMapping("/{postId}")
    @Operation(summary = "글 신고 취소")
    public RsData<Empty> delete(@PathVariable("postId") Long postId) {

        if (!postReportService.haveAuthority(postId))
            throw new GlobalException(CodeMsg.E400_6_CANCEL_REPORT_FAILED.getCode(),CodeMsg.E400_6_CANCEL_REPORT_FAILED.getMessage());

        postReportService.delete(postId);

        return RsData.of(Msg.E200_7_CANCEL_REPORT_SUCCEED.getCode(),
                Msg.E200_7_CANCEL_REPORT_SUCCEED.getMsg());
    }



}
