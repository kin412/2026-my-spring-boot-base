package com.kin.base.domain.board.service;

import com.kin.base.domain.board.dto.BoardDto;
import com.kin.base.domain.board.dto.BoardSearchCondition;
import com.kin.base.domain.board.entity.Board;
import com.kin.base.domain.board.entity.BoardFile;
import com.kin.base.domain.board.repository.BoardFileRepository;
import com.kin.base.domain.board.repository.BoardRepository;
import com.kin.base.domain.member.entity.Member;
import com.kin.base.domain.member.repository.MemberRepository;
import com.kin.base.global.common.FileStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final FileStore fileStore;
    private final BoardFileRepository boardFileRepository;

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

        Board board = boardRepository.findByIdWithAuthorAndBoardFiles(id).orElseThrow(
                () -> new IllegalArgumentException("잘못된 게시글 번호 입니다!")
        );

        return new BoardDto(board);

    }

    @Transactional
    public Long save(BoardDto boardDto, List<MultipartFile> boardFiles) throws IOException {

        Member author = memberRepository.findByLoginId(boardDto.getAuthor()).orElseThrow(
                ()-> new IllegalArgumentException("잘못된 사용자 ID입니다." + boardDto.getAuthor())
        );

        Board board = Board.builder()
                .type("BOARD")
                .title(boardDto.getTitle())
                .content(boardDto.getContent())
                .build();

        //해주는게 무결성 유지에 좋음. 지금은 board 인서트 시 author가 있어야 인서트가 되기때문에 필요하다.
        //member author를 db에 무슨작업을 하지 않을 거라면 그냥 author를 board에 넣어주기만 해도됨.
        //하지만 이렇게 연관관계 편의 메서드를 쓴다는 것 자체는 다른데서도 많이 쓴다는 얘기이므로 메서드 자체는 필요하다.
        board.changeAuthor(author);

        //첨부파일 처리
        List<BoardFile> storeFiles = fileStore.storeFiles(boardFiles);

        log.info("-=- storeFiles size : " + storeFiles.size());

        //연관관계 편의 메서드로 연관관계 맺기
        for(BoardFile boardFile : storeFiles) {
            boardFile.changeBoard(board);
        }

        //board만 저장해도 영속성 전이 cascade.all로 file도 같이저장
        return boardRepository.save(board).getId();


    }

    @Transactional
    public Long update(BoardDto boardDto, List<MultipartFile> boardFiles, List<Long> deleteFileIds) throws IOException {

        // 업데이트할때 작성자는 업데이트 안할거니까
        /*Member author = memberRepository.findByLoginId(boardDto.getAuthor()).orElseThrow(
                ()-> new IllegalArgumentException("잘못된 사용자 ID입니다." + boardDto.getAuthor())
        );*/

        Board board = boardRepository.findByIdWithAuthorAndBoardFiles(boardDto.getId()).orElseThrow(
                ()-> new IllegalArgumentException("잘못된 게시글 접근입니다."));

        //첨부파일 처리
        List<BoardFile> storeFiles = fileStore.storeFiles(boardFiles);

        log.info("-=- storeFiles size : " + storeFiles.size());

        //연관관계 편의 메서드로 연관관계 맺기
        for(BoardFile boardFile : storeFiles) {
            boardFile.changeBoard(board);
        }


        //setter가 나쁜게 아니다. 정확한 의도가 있는 메서드를 만들어 사용하라.
        board.updateBoard(boardDto.getTitle(), boardDto.getContent(), "CONTENT");

        // 업데이트할때 작성자는 업데이트 안할거니까
        //board.changeAuthor(author);

        Long boardId = boardRepository.save(board).getId();

        //삭제처리된 파일 삭제
        if(deleteFileIds != null && deleteFileIds.size() > 0) {
            //deleteFileIds.forEach(id -> log.info("삭제할 파일 ID: {}", id));
            log.info("-=- deleteFileIds : {}", deleteFileIds);
            //deleteFileIds.forEach(boardService::deleteFileById);

            deleteFileIds.forEach(id -> {

                /*BoardFile boardFile =  boardFileRepository.findById(id).orElseThrow(
                        () -> new IllegalArgumentException("존재하지 않는 파일 ID입니다: " + id)
                );*/

                //cascade옵션을 준상태에서는 이렇게 해도 반대쪽이 살아있으므로 트랜잭션이 끝나는 시점에
                //다시 살아남.
                //boardFileRepository.deleteById(id);


                board.getBoardFiles().stream()
                        .filter(boardFile -> boardFile.getId().equals(id)).findFirst()
                        .ifPresent(file -> {

                            // 2. 부모 리스트에서 제거 (삭제의 핵심)
                            board.getBoardFiles().remove(file);

                            // 3. 자식 객체에서도 부모 참조 제거 (완벽한 관계 단절)
                            file.changeBoard(null);

                            //물리 파일 삭제
                            fileStore.deleteRealFile(file.getSaveName());
                        });

            });
        }

        return boardId;

    }

    public void delete(Long id) {

        Board board = boardRepository.findByIdWithAuthorAndBoardFiles(id).orElseThrow(
                () -> new IllegalArgumentException("잘못된 게시글 접근입니다.")
        );

        board.getBoardFiles().stream().forEach(boardFile -> {
            fileStore.deleteRealFile(boardFile.getSaveName());
        });

        boardRepository.deleteById(id);

    }

    public BoardFile findFileById(Long id){
        return boardFileRepository.findById(id).orElseThrow(
                ()-> new IllegalArgumentException("잘못된 파일입니다."));
    }

    public void deleteFileById(Long id){
        boardFileRepository.deleteById(id);
    }

    @Transactional
    public void deleteMultiple(List<Long> ids){

        //지워질 파일 목록 미리 가져오기. 스프링데이터jpa 쿼리 메소드
        List<BoardFile> deleteFiles = boardFileRepository.findByBoardIdIn(ids);

        boardRepository.deleteAllByIdInBatch(ids);

        String fileFullName = "";

        for(BoardFile file : deleteFiles){
            if(file.getSaveName() != null) {
                fileFullName = fileStore.getFullPath(file.getSaveName());
                fileStore.deleteRealFile(fileFullName);
            }

        }


    }

}
