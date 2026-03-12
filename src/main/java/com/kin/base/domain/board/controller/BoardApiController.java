package com.kin.base.domain.board.controller;

import com.kin.base.domain.board.dto.BoardDto;
import com.kin.base.domain.board.dto.BoardSearchCondition;
import com.kin.base.domain.board.service.BoardService;
import com.kin.base.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Tag(name = "BoardApi", description = "게시판 관련 REST API")
@Slf4j
@RestController
@RequestMapping("/api/v1/boards") // 버전 관리(v1)를 포함하는 것이 관례
@RequiredArgsConstructor
public class BoardApiController {

    private final BoardService boardService;

    // 1. 게시글 목록 조회 (GET)
    @GetMapping
    public ResponseEntity<ApiResponse<Page<BoardDto>>> getBoardList(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            @ModelAttribute BoardSearchCondition condition) {

        Page<BoardDto> boardList = boardService.findAll(pageable, condition);
        return ResponseEntity.ok(ApiResponse.success(boardList));
    }

    // 2. 게시글 상세 조회 (GET)
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BoardDto>> getBoardDetail(@PathVariable Long id) {
        // 내부에서 예외가 터지면 @RestControllerAdvice가 잡음 (컨트롤러는 비즈니스만 집중)
        BoardDto boardDto = boardService.findById(id);
        return ResponseEntity.ok(ApiResponse.success(boardDto));
    }

    // 3. 게시글 등록 (POST)
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Long>> createBoard(
            @Valid @RequestPart("board") BoardDto boardDto,
            @RequestPart(value = "boardFiles", required = false) List<MultipartFile> boardFiles) throws IOException {

        Long id = boardService.save(boardDto, boardFiles);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(id));
    }

    // 4. 게시글 수정 (PUT / PATCH)
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<Long>> updateBoard(
            @PathVariable Long id,
            @Valid @RequestPart("board") BoardDto boardDto,
            @RequestParam(value = "deleteFileIds", required = false) List<Long> deleteFileIds,
            @RequestPart(value = "boardFiles", required = false) List<MultipartFile> boardFiles) throws IOException {

        boardDto.setId(id); // URI의 ID를 우선시
        Long updatedId = boardService.update(boardDto, boardFiles, deleteFileIds);
        return ResponseEntity.ok(ApiResponse.success(updatedId));
    }

    // 5. 게시글 삭제 (DELETE)
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBoard(@PathVariable Long id) {
        boardService.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @DeleteMapping("/adminSelectDel")
    public ResponseEntity<String> adminSelectDel(@RequestBody List<Long> ids) {

        boardService.deleteMultiple(ids);

        return ResponseEntity.ok("success");
    }

}
