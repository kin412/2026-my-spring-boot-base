package com.kin.base.domain.board.controller;

import com.kin.base.domain.board.service.BoardService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "BoardApi", description = "게시판 관련 API")
@Slf4j
@RestController
@RequestMapping("/boardApi")
@RequiredArgsConstructor
public class BoardApiController {

    private final BoardService boardService;

    @DeleteMapping("/adminSelectDel")
    public ResponseEntity<String> adminSelectDel(@RequestBody List<Long> ids) {

        boardService.deleteMultiple(ids);

        return ResponseEntity.ok("success");
    }

}
