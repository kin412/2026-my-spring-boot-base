package com.kin.base.domain.board.service;

import com.kin.base.domain.board.dto.BoardDto;
import com.kin.base.domain.board.dto.BoardSearchCondition;
import com.kin.base.domain.board.entity.Board;
import com.kin.base.domain.board.repository.BoardRepository;
import com.kin.base.domain.member.entity.Member;
import com.kin.base.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

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

    public BoardDto findById(Long id) {

        Board board = boardRepository.findByIdWithAuthor(id).orElseThrow(
                () -> new IllegalArgumentException("잘못된 게시글 번호 입니다!")
        );

        return new BoardDto(board);

    }

    @Transactional
    public Long save(BoardDto boardDto) {

        Member author = memberRepository.findByLoginId(boardDto.getAuthor()).orElseThrow(
                ()-> new IllegalArgumentException("잘못된 사용자 ID입니다." + boardDto.getAuthor())
        );


        Board board = Board.builder()
                .type("BOARD")
                .title(boardDto.getTitle())
                .content(boardDto.getContent())
                .build();

        board.changeAuthor(author);

        return boardRepository.save(board).getId();


    }

    @Transactional
    public Long update(BoardDto boardDto) {

        Member author = memberRepository.findByLoginId(boardDto.getAuthor()).orElseThrow(
                ()-> new IllegalArgumentException("잘못된 사용자 ID입니다." + boardDto.getAuthor())
        );

        Board board = boardRepository.findByIdWithAuthor(boardDto.getId()).orElseThrow(
                ()-> new IllegalArgumentException("잘못된 게시글 접근입니다."));

        //setter가 나쁜게 아니다. 정확한 의도가 있는 메서드를 만들어 사용하라.
        board.updateBoard(boardDto.getTitle(), boardDto.getContent(), "CONTENT");

        //해주는게 무결성 유지에 좋음. 지금은 board 업데이트 시 author가 있어야 업데이트가 되기때문에 필요하다.
        //member author를 db에 무슨작업을 하지 않을 거라면 그냥 author를 board에 넣어주기만 해도됨.
        //하지만 이렇게 연관관계 편의 메서드를 쓴다는 것 자체는 다른데서도 많이 쓴다는 얘기이므로 메서드 자체는 필요하다.
        board.changeAuthor(author);

        return boardRepository.save(board).getId();

    }

    public void delete(Long id) {
        boardRepository.deleteById(id);
    }

}
