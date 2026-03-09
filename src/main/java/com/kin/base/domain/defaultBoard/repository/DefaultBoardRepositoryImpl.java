package com.kin.base.domain.defaultBoard.repository;

import com.kin.base.domain.defaultBoard.entity.DefaultBoard;
import com.kin.base.domain.defaultBoard.entity.QDefaultBoard;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.kin.base.domain.defaultBoard.entity.QDefaultBoard.defaultBoard;
import static org.springframework.util.StringUtils.hasText;

@RequiredArgsConstructor
public class DefaultBoardRepositoryImpl implements DefaultBoardRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<DefaultBoard> searchQuerydsl(String title, String author, LocalDateTime start, LocalDateTime end, Pageable pageable) {

        //static import 가능
        //QDefaultBoard defaultBoard = QDefaultBoard.defaultBoard;

        List<DefaultBoard> defaultBoardList = queryFactory
                .selectFrom(defaultBoard)
                .where(
                    titleContains(title),
                        authorEq(author),
                        betweenDate(start,end)
                )
                .offset(pageable.getOffset()) //몇번째부터
                .limit(pageable.getPageSize()) //몇개나
                .orderBy(getOrderSpecifiers(pageable))
                .fetch();

        // 2. 전체 개수 쿼리 (페이징용)
        Long total = queryFactory
                .select(defaultBoard.count())
                .from(defaultBoard)
                .where(
                        titleContains(title),
                        authorEq(author),
                        betweenDate(start, end)
                )
                .fetchOne();

        return new PageImpl<>(defaultBoardList, pageable, total != null ? total : 0L);


    }

    // 동적 쿼리용 조건 메서드
    
    private BooleanExpression titleContains(String title) {
        return hasText(title) ? defaultBoard.title.contains(title) : null;

    }

    private BooleanExpression authorEq(String author) {
        return hasText(author) ? defaultBoard.author.contains(author) : null;
    }

    private BooleanExpression betweenDate(LocalDateTime start, LocalDateTime end) {
        return (start != null && end != null) ? defaultBoard.createdDate.between(start, end) : null;
    }

    //정렬 유틸 메서드

    private OrderSpecifier<?>[] getOrderSpecifiers(Pageable pageable) {

        List<OrderSpecifier<?>> orders = new ArrayList<>();

        for (Sort.Order order : pageable.getSort()) {

            Order direction = order.isAscending() ? Order.ASC : Order.DESC;

            PathBuilder<DefaultBoard> pathBuilder =
                    new PathBuilder<>(DefaultBoard.class, defaultBoard.getMetadata());

            orders.add(new OrderSpecifier(direction, pathBuilder.get(order.getProperty())));
        }

        return orders.toArray(new OrderSpecifier[0]);
    }

}
