package com.ll.edubridge.domain.post.comment.repository;

import com.ll.edubridge.domain.post.comment.entity.Comment;
import com.ll.edubridge.domain.post.comment.entity.QComment;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;


@RequiredArgsConstructor
public class CustomCommentRepositoryImpl implements CustomCommentRepository{
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Comment> findBestComment(Long postId) {
        QComment comment = QComment.comment;
        List<Comment> topComments = queryFactory
                .selectFrom(comment)
                .where(comment.voteCount.goe(5).and(comment.post.id.eq(postId))) // voteCount가 5 이상인 조건
                .orderBy(comment.voteCount.desc()) // voteCount 기준 내림차순 정렬
                .limit(2) // 상위 2개만 조회
                .fetch();
        return topComments;
    }
}
