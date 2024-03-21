package com.ll.edubridge.domain.course.roadmap.repository;

import com.ll.edubridge.domain.course.roadmap.entity.Roadmap;
import com.ll.edubridge.standard.base.KwTypeCourse;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;

import static com.ll.edubridge.domain.course.roadmap.entity.QRoadmap.roadmap;

@RequiredArgsConstructor
public class CustomRoadmapRepositoryImpl implements CustomRoadmapRepository {

    private final JPAQueryFactory queryFactory;
    
    @Override
    public Page<Roadmap> findByKw(KwTypeCourse kwType, String kw, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();

        if (kw != null && !kw.isBlank()) {
            applyKeywordFilter(kwType, kw, builder);
        }

        JPAQuery<Roadmap> roadmapsQuery = createRoadmapQuery(builder);
        applySorting(pageable, roadmapsQuery);

        roadmapsQuery.offset(pageable.getOffset()).limit(pageable.getPageSize());

        JPAQuery<Long> totalQuery = createTotalQuery(builder);

        return PageableExecutionUtils.getPage(roadmapsQuery.fetch(), pageable, totalQuery::fetchOne);
    }

    private void applyKeywordFilter(KwTypeCourse kwType, String kw, BooleanBuilder builder) {
        switch (kwType) {
            case kwType.TITLE -> builder.and(roadmap.title.containsIgnoreCase(kw));
            case kwType.HASHTAGS -> builder.and(roadmap.hashtags.containsIgnoreCase(kw));
            case kwType.NAME -> builder.and(roadmap.owner.username.containsIgnoreCase(kw));
            default -> {
                builder.andAnyOf(
                        roadmap.title.containsIgnoreCase(kw),
                        roadmap.hashtags.containsIgnoreCase(kw),
                        roadmap.owner.username.containsIgnoreCase(kw)
                );
            }
        }
    }

    private JPAQuery<Roadmap> createRoadmapQuery(BooleanBuilder builder) {
        return queryFactory
                .select(roadmap)
                .from(roadmap)
                .where(builder);
    }

    private void applySorting(Pageable pageable, JPAQuery<Roadmap> roadmapQuery) {
        for (Sort.Order o : pageable.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(roadmap.getType(), roadmap.getMetadata());
            roadmapQuery.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC, pathBuilder.get(o.getProperty())));
        }
    }

    private JPAQuery<Long> createTotalQuery(BooleanBuilder builder) {
        return queryFactory
                .select(roadmap.count())
                .from(roadmap)
                .where(builder);
    }

}
