package com.ll.edubridge.global.sse;

import com.ll.edubridge.domain.course.summaryNote.entity.SummaryNote;
import com.ll.edubridge.domain.course.summaryNote.repository.SummaryNoteRepository;
import com.ll.edubridge.domain.member.member.repository.MemberRepository;
import com.ll.edubridge.domain.post.post.entity.Post;
import com.ll.edubridge.domain.post.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final SummaryNoteRepository summaryNoteRepository;

    // 메시지 알림
    public SseEmitter subscribe(Long userId) {


        // 1. 현재 클라이언트를 위한 sseEmitter 객체 생성
        SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);
        
        // 2. 연결
        try {
            sseEmitter.send(SseEmitter.event().name("connect"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 3. 저장
        NotificationController.sseEmitters.put(userId, sseEmitter);

		// 4. 연결 종료 처리
        sseEmitter.onCompletion(() -> NotificationController.sseEmitters.remove(userId));	// sseEmitter 연결이 완료될 경우
        sseEmitter.onTimeout(() -> NotificationController.sseEmitters.remove(userId));		// sseEmitter 연결에 타임아웃이 발생할 경우
        sseEmitter.onError((e) -> NotificationController.sseEmitters.remove(userId));		// sseEmitter 연결에 오류가 발생할 경우

        return sseEmitter;
    }


    
    // 댓글 알림 - 게시글 작성자 에게
    public void notifyComment(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("게시글을 찾을 수 없습니다.")
        );

        Long userId = post.getWriter().getId();
        if (NotificationController.sseEmitters.containsKey(userId)) {
            SseEmitter sseEmitter = NotificationController.sseEmitters.get(userId);
            try {
                sseEmitter.send(SseEmitter.event().name("addComment").data("댓글이 달렸습니다."));
            } catch (Exception e) {
                NotificationController.sseEmitters.remove(userId);
            }
        }
    }

    // 포인트 알림 - 요약노트 작성자 에게
    public void notifySummaryNotePoint(Long noteId) {
        SummaryNote summaryNote = summaryNoteRepository.findById(noteId).orElseThrow(
                () -> new IllegalArgumentException("요약노트를 찾을 수 없습니다.")
        );

        Long userId = summaryNote.getWriter().getId();
        if (NotificationController.sseEmitters.containsKey(userId)) {
            SseEmitter sseEmitter = NotificationController.sseEmitters.get(userId);
            try {
                sseEmitter.send(SseEmitter.event().name("addSummaryNotePoint").data("요약노트 포인트가 지급되었습니다."));
            } catch (Exception e) {
                NotificationController.sseEmitters.remove(userId);
            }
        }
    }
}

