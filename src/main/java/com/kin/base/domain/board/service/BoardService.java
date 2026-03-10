package com.kin.base.domain.board.service;

import com.kin.base.domain.board.dto.BoardDto;
import com.kin.base.domain.board.dto.BoardSearchCondition;
import com.kin.base.domain.board.entity.Board;
import com.kin.base.domain.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    public Page<BoardDto> findAll(Pageable pageable, BoardSearchCondition boardSearchCondition) {

        LocalDateTime searchStartDate = null;
        LocalDateTime searchEndDate = null;

        if (StringUtils.hasText(boardSearchCondition.getCreatedDate())) {
            // "2026-03-19" 문자열을 해당 날짜의 0시 0분으로 변환
            searchStartDate = LocalDate.parse(boardSearchCondition.getCreatedDate()).atStartOfDay();
            searchEndDate = LocalDate.parse(boardSearchCondition.getCreatedDate()).atTime(LocalTime.MAX);
        }

        Page<Board> board = boardRepository.searchQuerydsl(boardSearchCondition.getTitle(), boardSearchCondition.getAuthor()
                , searchStartDate, searchEndDate, pageable);

        return board.map(BoardDto::new);
    }

}
