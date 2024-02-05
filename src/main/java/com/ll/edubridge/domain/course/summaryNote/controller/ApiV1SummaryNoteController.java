package com.ll.edubridge.domain.course.summaryNote.controller;

import com.ll.edubridge.domain.course.summaryNote.dto.CreateSummaryNoteDto;
import com.ll.edubridge.domain.course.summaryNote.dto.SummaryNoteDto;
import com.ll.edubridge.domain.course.summaryNote.entity.SummaryNote;
import com.ll.edubridge.domain.course.summaryNote.service.SummaryNoteService;
import com.ll.edubridge.domain.member.member.entity.Member;
import com.ll.edubridge.domain.openai.ChatService;
import com.ll.edubridge.global.app.AppConfig;
import com.ll.edubridge.global.exceptions.CodeMsg;
import com.ll.edubridge.global.exceptions.GlobalException;
import com.ll.edubridge.global.msg.Msg;
import com.ll.edubridge.global.rq.Rq;
import com.ll.edubridge.global.rsData.RsData;
import com.ll.edubridge.standard.base.Empty;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/courses/{videoid}/note", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "ApiV1SummaryNoteController", description = "강의 요약노트 CRUD 컨트롤러")
public class ApiV1SummaryNoteController {

    private final SummaryNoteService summaryNoteService;
    private final Rq rq;
    private final ChatService chatService;

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

    @PostMapping("")
    @Operation(summary = "강의 요약 노트 등록")
    public RsData<SummaryNoteDto> create(@RequestBody CreateSummaryNoteDto createSummaryNoteDto,@PathVariable Long videoid) {
        SummaryNote summaryNote = summaryNoteService.create(rq.getMember(), createSummaryNoteDto,videoid);
        String keywords = summaryNote.getVideo().getKeywords();
        chatService.chat(summaryNote.getId(),createSummaryNoteDto.getContent() + "\n 이 문단을 키워드:"+keywords+ "와 문단의 문맥을 기준으로해서 총점100점기준으로 채점해서 부가적인 설명을 하지말고 20점이면 20,30점이면 30처럼 점수만을 알려줘");
        SummaryNoteDto summaryNoteDto = new SummaryNoteDto(summaryNote);

        return RsData.of(Msg.E200_0_CREATE_SUCCUSSED.getCode(), Msg.E200_0_CREATE_SUCCUSSED.getMsg(), summaryNoteDto);


    }

    @PutMapping("/{noteId}")
    @Operation(summary = "강의 요약 노트 수정")
    public RsData<SummaryNoteDto> modify(
            @PathVariable("noteId") Long id,
            @RequestBody CreateSummaryNoteDto createSummaryNoteDto) {

        if(!summaryNoteService.haveAuthority(id))
            throw new GlobalException(CodeMsg.E403_1_NO.getCode(), CodeMsg.E403_1_NO.getMessage());

        SummaryNote modifySummaryNote = summaryNoteService.modify(id, createSummaryNoteDto);

        SummaryNoteDto modifySummaryNoteDto = new SummaryNoteDto(modifySummaryNote);

        return RsData.of(Msg.E200_2_MODIFY_SUCCUSSED.getCode(), Msg.E200_2_MODIFY_SUCCUSSED.getMsg(), modifySummaryNoteDto);


    }

    @DeleteMapping("/{noteId}")
    @Operation(summary = "강의 요약 노트 삭제")
    public RsData<Empty> delete(@PathVariable("noteId") Long id) {

        if(!summaryNoteService.haveAuthority(id))
            throw new GlobalException(CodeMsg.E403_1_NO.getCode(), CodeMsg.E403_1_NO.getMessage());

        summaryNoteService.delete(id);

        return RsData.of(Msg.E200_3_DELETE_SUCCUSSED.getCode(), Msg.E200_3_DELETE_SUCCUSSED.getMsg());
    }
    @GetMapping("/admin")
    @Operation(summary = "비디오별 강의노트 조회(관리자 기능)")
    public RsData<GetSummaryNoteResponsebody> getSummaryNoteAdmin(@RequestParam(value = "page", defaultValue = "1") int page,@PathVariable Long videoid){
        int pageSize = AppConfig.getBasePageSize();
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Direction.ASC, "id"));

        Page<SummaryNote> summaryNotes = summaryNoteService.findByVideoId(pageable,videoid);
        GetSummaryNoteResponsebody responseBody = new ApiV1SummaryNoteController.GetSummaryNoteResponsebody(summaryNotes, rq.getMember());

        return RsData.of(Msg.E200_1_INQUIRY_SUCCUSSED.getCode(), Msg.E200_1_INQUIRY_SUCCUSSED.getMsg(), responseBody);
    }


    @GetMapping("/{note-id}")
    @Operation(summary = "강의 요약 노트 상세 보기")
    public RsData<SummaryNoteDto> getSummaryNote(@PathVariable("note-id") Long id){
        SummaryNote summaryNote = summaryNoteService.getSummaryNote(id);
        SummaryNoteDto summaryNoteDto = new SummaryNoteDto(summaryNote);
        return RsData.of(Msg.E200_1_INQUIRY_SUCCUSSED.getCode(), Msg.E200_1_INQUIRY_SUCCUSSED.getMsg(), summaryNoteDto);
    }
}