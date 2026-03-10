package com.kin.base.domain.board.repository;

import com.kin.base.domain.board.entity.Board;
import com.kin.base.domain.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface BoardRepositoryCustom {

    Page<Board> searchQuerydsl(String title,
                               String author,
                               LocalDateTime start,
                               LocalDateTime end,
                               Pageable pageable);

}
