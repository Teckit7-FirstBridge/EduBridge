package com.ll.edubridge.domain.course.summaryNote.controller;

import com.ll.edubridge.domain.course.summaryNote.dto.CreateSummaryNoteDto;
import com.ll.edubridge.domain.course.summaryNote.dto.SummaryNoteDto;
import com.ll.edubridge.domain.course.summaryNote.entity.SummaryNote;
import com.ll.edubridge.domain.course.summaryNote.service.SummaryNoteService;
import com.ll.edubridge.domain.member.member.entity.Member;
import com.ll.edubridge.global.app.AppConfig;
import com.ll.edubridge.global.exceptions.GlobalException;
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
@RequestMapping(value = "/api/v1/courses/{video-id}/note", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "ApiV1SummaryNoteController", description = "강의 요약노트 CRUD 컨트롤러")
public class ApiV1SummaryNoteController {
    private final SummaryNoteService summaryNoteService;
    private final Rq rq;

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
    public RsData<SummaryNoteDto> create(@RequestBody CreateSummaryNoteDto createSummaryNoteDto) {
        SummaryNote summaryNote = summaryNoteService.create(rq.getMember(), createSummaryNoteDto);

        SummaryNoteDto summaryNoteDto = new SummaryNoteDto(summaryNote);

        return RsData.of("200-0", "등록 성공", summaryNoteDto);
    }

    @PutMapping("/{note-id}")
    @Operation(summary = "강의 요약 노트 수정")
    public RsData<SummaryNoteDto> modify(
            @PathVariable("id") Long id,
            @RequestBody SummaryNoteDto summaryNoteDto) {

        if(!summaryNoteService.haveAuthority(id))
            throw new GlobalException("403-1", "권한이 없습니다.");

        SummaryNote modifySummaryNote = summaryNoteService.modify(id, summaryNoteDto);

        SummaryNoteDto modifySummaryNoteDto = new SummaryNoteDto(modifySummaryNote);

        return RsData.of("200-2", "수정 성공", modifySummaryNoteDto);
    }

    @DeleteMapping("/{note-id}")
    @Operation(summary = "강의 요약 노트 삭제")
    public RsData<Empty> delete(@PathVariable("id") Long id) {

        if(!summaryNoteService.haveAuthority(id))
            throw new GlobalException("403-1", "권한이 없습니다.");

        summaryNoteService.delete(id);

        return RsData.of("200-3", "삭제 성공");
    }

    @GetMapping("")
    @Operation(summary = "전체 강의노트 목록 조회")
    public RsData<GetSummaryNoteResponsebody> getSummaryNote(@RequestParam(value = "page", defaultValue = "1") int page){
        int pageSize = AppConfig.getBasePageSize();
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Direction.ASC, "id"));

        Page<SummaryNote> summaryNotes = summaryNoteService.findAll(pageable);
        GetSummaryNoteResponsebody responseBody = new ApiV1SummaryNoteController.GetSummaryNoteResponsebody(summaryNotes, rq.getMember());

        return RsData.of(
                "200-1",
                "성공",
                responseBody
        );
    }

    @GetMapping("/{note-id}")
    @Operation(summary = "강의 요약 노트 상세 보기")
    public RsData<SummaryNoteDto> getSummaryNote(@PathVariable("{note-id}") Long id){
        SummaryNote summaryNote = summaryNoteService.getSummaryNote(id);
        SummaryNoteDto summaryNoteDto = new SummaryNoteDto(summaryNote);
        return RsData.of("200-1", "성공", summaryNoteDto);
    }
}