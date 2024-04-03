package com.ll.edubridge.domain.course.summaryNote.controller;

import com.ll.edubridge.domain.course.summaryNote.dto.CreateSummaryNoteDto;
import com.ll.edubridge.domain.course.summaryNote.dto.SummaryNoteDto;
import com.ll.edubridge.domain.course.summaryNote.entity.SummaryNote;
import com.ll.edubridge.domain.course.summaryNote.service.SummaryNoteService;
import com.ll.edubridge.domain.member.member.entity.Member;
import com.ll.edubridge.domain.member.member.service.MemberService;
import com.ll.edubridge.domain.notification.service.NotificationService;
import com.ll.edubridge.domain.openai.ChatService;
import com.ll.edubridge.domain.point.point.service.PointService;
import com.ll.edubridge.global.app.AppConfig;
import com.ll.edubridge.global.exceptions.CodeMsg;
import com.ll.edubridge.global.exceptions.GlobalException;
import com.ll.edubridge.global.msg.Msg;
import com.ll.edubridge.global.rq.Rq;
import com.ll.edubridge.global.rsData.RsData;
import com.ll.edubridge.standard.base.Empty;
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

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/courses", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "ApiV1SummaryNoteController", description = "강의 요약노트 CRUD 컨트롤러")
public class ApiV1SummaryNoteController {

    private final SummaryNoteService summaryNoteService;
    private final Rq rq;
    private final ChatService chatService;
    private final NotificationService notificationService;
    private final PointService pointService;
    private final MemberService memberService;

    @Getter
    public static class GetSummaryNoteResponsebody{
        @NonNull
        private final List<SummaryNoteDto> items;

        public GetSummaryNoteResponsebody(Page<SummaryNote> page, Member currentUser) {
            this.items = page.getContent().stream()
                    .map(summaryNote -> new SummaryNoteDto(summaryNote))
                    .toList();
        }
    }

    @PostMapping("/{videoId}/note")
    @Operation(summary = "강의 요약 노트 등록")
    public RsData<SummaryNoteDto> create(@Valid @RequestBody CreateSummaryNoteDto createSummaryNoteDto, @PathVariable Long videoId) {
        SummaryNote summaryNote = summaryNoteService.create(rq.getMember(), createSummaryNoteDto, videoId);
        String keywords = summaryNote.getVideo().getKeywords();
        chatService.chat(summaryNote.getId(),createSummaryNoteDto.getContent() + "\n이 문단을 채점해줘. 총점 100점이 만점이야. 각 채점 기준과 기준 점수는 다음과 같아. 1) 다음의 키워드가 잘 들어갔는가. 키워드:" + keywords + " - 만점 30점.  2) 각 문장이 문맥이 매끄러운가. - 만점 20점. 3) 해당 키워드가 가진 프로그래밍 개념이 제대로 반영되었는가. - 만점 50점. 조건을 제대로 충족하지 못했다면 점수를 아주 낮게 줘. 종합 만점은 100점이야. 다른 부가적인 설명은 하지마. 세부 기준에 대한 평가도 하지마. 총점만 말해줘. '20점'처럼 오직 점수만 알려줘. 내 요약 실력을 높이기 위한 공부야. 아주 엄격하게 채점해줘.",rq.getMember());
        SummaryNoteDto summaryNoteDto = new SummaryNoteDto(summaryNote);

        return RsData.of(Msg.E200_0_CREATE_SUCCEED.getCode(), Msg.E200_0_CREATE_SUCCEED.getMsg(), summaryNoteDto);
    }

    @PutMapping("/{videoId}/note/{noteId}")
    @Operation(summary = "강의 요약 노트 수정")
    public RsData<SummaryNoteDto> modify(
            @PathVariable("noteId") Long id,
            @RequestBody CreateSummaryNoteDto createSummaryNoteDto) {
        if(summaryNoteService.getSummaryNote(id).getScore()>=AppConfig.SummaryPassScore){
            throw new GlobalException(CodeMsg.E400_8_ALREADY_PASSED.getCode(),CodeMsg.E400_8_ALREADY_PASSED.getMessage());
        }
        if(!summaryNoteService.haveAuthority(id))
            throw new GlobalException(CodeMsg.E403_1_NO.getCode(), CodeMsg.E403_1_NO.getMessage());

        SummaryNote modifySummaryNote = summaryNoteService.modify(id, createSummaryNoteDto);
        String keywords = modifySummaryNote.getVideo().getKeywords();
        chatService.chat(modifySummaryNote.getId(),createSummaryNoteDto.getContent() + "\n이 문단을 채점해줘. 총점 100점이 만점이야. 각 채점 기준과 기준 점수는 다음과 같아. 1) 다음의 키워드가 잘 들어갔는가. 키워드:" + keywords + " - 만점 30점.  2) 각 문장이 문맥이 매끄러운가. - 만점 20점. 3) 해당 키워드가 가진 프로그래밍 개념이 제대로 반영되었는가. - 만점 50점. 조건을 제대로 충족하지 못했다면 점수를 아주 낮게 줘. 종합 만점은 100점이야. 다른 부가적인 설명은 하지마. 세부 기준에 대한 평가도 하지마. 총점만 말해줘. '20점'처럼 오직 점수만 알려줘. 내 요약 실력을 높이기 위한 공부야. 아주 엄격하게 채점해줘.",rq.getMember());

        SummaryNoteDto modifySummaryNoteDto = new SummaryNoteDto(modifySummaryNote);

        return RsData.of(Msg.E200_2_MODIFY_SUCCEED.getCode(), Msg.E200_2_MODIFY_SUCCEED.getMsg(), modifySummaryNoteDto);
    }

    @DeleteMapping("/{videoId}/note/{noteId}")
    @Operation(summary = "강의 요약 노트 삭제")
    public RsData<Empty> delete(@PathVariable("noteId") Long id) {
        if(summaryNoteService.getSummaryNote(id).getScore()>=AppConfig.SummaryPassScore){
            throw new GlobalException(CodeMsg.E400_8_ALREADY_PASSED.getCode(),CodeMsg.E400_8_ALREADY_PASSED.getMessage());
        }

        if(!summaryNoteService.haveAuthority(id))
            throw new GlobalException(CodeMsg.E403_1_NO.getCode(), CodeMsg.E403_1_NO.getMessage());

        summaryNoteService.delete(id);

        return RsData.of(Msg.E200_3_DELETE_SUCCEED.getCode(), Msg.E200_3_DELETE_SUCCEED.getMsg());
    }

    @GetMapping("/{videoId}/note/admin")
    @Operation(summary = "비디오별 강의노트 조회(관리자 기능)")
    public RsData<GetSummaryNoteResponsebody> getSummaryNoteAdmin(@RequestParam(value = "page", defaultValue = "1") int page,@PathVariable Long videoId){
        int pageSize = AppConfig.getBasePageSize();
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Direction.ASC, "id"));

        Page<SummaryNote> summaryNotes = summaryNoteService.findByVideoId(pageable, videoId);
        GetSummaryNoteResponsebody responseBody = new ApiV1SummaryNoteController.GetSummaryNoteResponsebody(summaryNotes, rq.getMember());

        return RsData.of(Msg.E200_1_INQUIRY_SUCCEED.getCode(), Msg.E200_1_INQUIRY_SUCCEED.getMsg(), responseBody);
    }

    @GetMapping("/{videoId}/note/{noteId}")
    @Operation(summary = "강의 요약 노트 상세 보기")
    public RsData<SummaryNoteDto> getSummaryNote(@PathVariable("noteId") Long id){
        SummaryNote summaryNote = summaryNoteService.getSummaryNote(id);
        SummaryNoteDto summaryNoteDto = new SummaryNoteDto(summaryNote);
        return RsData.of(Msg.E200_1_INQUIRY_SUCCEED.getCode(), Msg.E200_1_INQUIRY_SUCCEED.getMsg(), summaryNoteDto);
    }

    @GetMapping("/summary/{writerId}")
    @Operation(summary = "작성자별 강의 요약노트 조회 ")
    public RsData<List<SummaryNoteDto>> getSummaryNoteByWriter(@PathVariable("writerId") Long id){
        List<SummaryNoteDto> byWriterId = summaryNoteService.findByWriterId(id).stream().map(SummaryNoteDto::new).collect(Collectors.toList());
        return RsData.of(Msg.E200_1_INQUIRY_SUCCEED.getCode(), Msg.E200_1_INQUIRY_SUCCEED.getMsg(),byWriterId);
    }


    @Getter
    public static class GetSummaryandMemberNoteResponsebody{
        @NonNull
        private final List<SummaryNoteDto> items;

        @NonNull
        private String member_nickname;
        public GetSummaryandMemberNoteResponsebody(List<SummaryNoteDto> items,String member_nickname) {
            this.items = items;
            this.member_nickname = member_nickname;
        }
    }


    @GetMapping("/summary")
    @Operation(summary = "작성자별(uuid) 강의 요약노트 조회 ")
    public RsData<GetSummaryandMemberNoteResponsebody> getSummaryNoteByUUID(@RequestParam("uuid") String uuid){
        Member member = memberService.findByUUID(uuid).get();
        List<SummaryNoteDto> byUuid = summaryNoteService.findByWriterId(member.getId()).stream().map(SummaryNoteDto::new).collect(Collectors.toList());

        return RsData.of(Msg.E200_1_INQUIRY_SUCCEED.getCode(), Msg.E200_1_INQUIRY_SUCCEED.getMsg(),new GetSummaryandMemberNoteResponsebody(byUuid,member.getNickname()));
    }



    @GetMapping("/summary/{writerId}/{courseId}")
    @Operation(summary = "작성자별 강좌의 모든 요약노트 목록 조회 ")
    public RsData<List<SummaryNoteDto>> getSummaryNoteByWriterandCourse(@PathVariable("writerId") Long memberId, @PathVariable("courseId") Long courseId){
        List<SummaryNoteDto> courseNotes = summaryNoteService.findByWriterIdAndCourseId(memberId, courseId).stream().map(SummaryNoteDto::new).collect(Collectors.toList());
        return RsData.of(Msg.E200_1_INQUIRY_SUCCEED.getCode(), Msg.E200_1_INQUIRY_SUCCEED.getMsg(), courseNotes);
    }
}