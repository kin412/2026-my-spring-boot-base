package com.kin.base.domain.defaultBoard.repository;

import com.kin.base.domain.defaultBoard.entity.DefaultBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface DefaultBoardRepositoryCustom {

    Page<DefaultBoard> searchQuerydsl(String title,
                              String author,
                              LocalDateTime start,
                              LocalDateTime end,
                              Pageable pageable);
}
