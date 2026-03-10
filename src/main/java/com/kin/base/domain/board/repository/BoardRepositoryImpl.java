package com.kin.base.domain.board.repository;

import com.kin.base.domain.board.entity.Board;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
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

import static com.kin.base.domain.board.entity.QBoard.board;
import static com.kin.base.domain.member.entity.QMember.member;
import static org.springframework.util.StringUtils.hasText;

@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Board> searchQuerydsl(String title, String author, LocalDateTime start, LocalDateTime end, Pageable pageable) {
        List<Board> boardList = queryFactory
                .selectFrom(board)
                .join(board.author, member).fetchJoin()
                .where(
                        titleContains(title),
                        authorContains(author),
                        betweenDate(start, end)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(getOrderSpecifiers(pageable))
                .fetch();

        Long total = queryFactory
                .select(board.count())
                .from(board)
                .join(board.author, member)
                .where(
                        titleContains(title),
                        authorContains(author),
                        betweenDate(start, end)
                ).fetchOne();

        return new PageImpl<>(boardList, pageable, total != null ? total : 0L);

    }

    private BooleanExpression titleContains(String title) {
        return hasText(title) ? board.title.contains(title) : null;
    }

    private BooleanExpression authorContains(String author) {
        return hasText(author) ? member.loginId.contains(author) : null;
    }

    private BooleanExpression betweenDate(LocalDateTime start, LocalDateTime end) {
        return (start != null && end != null) ? board.createdDate.between(start, end) : null;
    }

    //정렬 유틸 메서드

    private OrderSpecifier<?>[] getOrderSpecifiers(Pageable pageable) {

        List<OrderSpecifier<?>> orders = new ArrayList<>();

        for (Sort.Order order : pageable.getSort()) {

            Order direction = order.isAscending() ? Order.ASC : Order.DESC;

            PathBuilder<Board> pathBuilder =
                    new PathBuilder<>(Board.class, board.getMetadata());

            orders.add(new OrderSpecifier(direction, pathBuilder.get(order.getProperty())));
        }

        return orders.toArray(new OrderSpecifier[0]);
    }

}
