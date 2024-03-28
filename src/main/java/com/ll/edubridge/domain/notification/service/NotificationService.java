package com.ll.edubridge.domain.notification.service;

import com.ll.edubridge.domain.course.summaryNote.repository.SummaryNoteRepository;
import com.ll.edubridge.domain.member.member.entity.Member;
import com.ll.edubridge.domain.member.member.repository.MemberRepository;
import com.ll.edubridge.domain.notification.controller.NotificationController;
import com.ll.edubridge.domain.notification.entity.Notification;
import com.ll.edubridge.domain.notification.entity.NotificationType;
import com.ll.edubridge.domain.notification.repository.NotificationRepository;
import com.ll.edubridge.domain.post.comment.entity.Comment;
import com.ll.edubridge.domain.post.post.entity.Post;
import com.ll.edubridge.domain.post.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final SummaryNoteRepository summaryNoteRepository;
    private final NotificationRepository notificationRepository;

    @Transactional(readOnly = true)
    public List<Notification> findByMemberId(Long memberId){
        return notificationRepository.findByMemberId(memberId);

    }
    @Transactional
    public void readNoti(Long id) {
        List<Notification> byMemberId = notificationRepository.findByMemberId(id);
        byMemberId.forEach(notification -> notification.setRead(true));
    }

    @Transactional
    public void deleteByComment(Comment comment){
        Notification notification = notificationRepository.findByComment(comment);
        notificationRepository.delete(notification);
    }

    @Transactional
    public void createByComment(NotificationType type, Member member, Post post, Member sender, Comment comment) {

        Notification notification = Notification.builder()
                .type(type)
                .recipient_id(member.getId())
                .sender_id(sender.getId())
                .post(post)
                .comment(comment)
                .build();

        notificationRepository.save(notification);
    }

    @Transactional
    public void createByPoint(NotificationType type, Member member,int point) {

        Notification notification = Notification.builder()
                .type(type)
                .recipient_id(member.getId())
                .point(point)
                .build();

        notificationRepository.save(notification);
    }

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
    public void notifyComment(Long userId) {
        if (NotificationController.sseEmitters.containsKey(userId)) {
            SseEmitter sseEmitter = NotificationController.sseEmitters.get(userId);
            try {
                sseEmitter.send(SseEmitter.event().name("addComment").data("답변이 등록되었습니다."));
            } catch (Exception e) {
                NotificationController.sseEmitters.remove(userId);
            }
        }
    }

    // 포인트 알림 - 요약노트 작성자 에게
    public void notifySummaryNotePoint(Long userId) {
        if (NotificationController.sseEmitters.containsKey(userId)) {
            SseEmitter sseEmitter = NotificationController.sseEmitters.get(userId);
            try {
                sseEmitter.send(SseEmitter.event().name("addSummaryNotePoint").data("요약노트 포인트가 지급되었습니다."));
            } catch (Exception e) {
                NotificationController.sseEmitters.remove(userId);
            }
        }
    }

    // 포인트 알림 - 출석 당사자 에게
    public void notifyAttendPoint(Long userId) {
        if (NotificationController.sseEmitters.containsKey(userId)) {
            SseEmitter sseEmitter = NotificationController.sseEmitters.get(userId);
            try {
                sseEmitter.send(SseEmitter.event().name("addAttendPoint").data("출석 포인트가 지급되었습니다."));
            } catch (Exception e) {
                NotificationController.sseEmitters.remove(userId);
            }
        }
    }


    public boolean isAlarm(Member member) {
        List<Notification> byMemberId = notificationRepository.findByMemberId(member.getId());
        if( byMemberId.stream().filter(notification -> notification.isRead() == false).count()>0){
            return true;
        }else{
            return false;
        }
    }
}

