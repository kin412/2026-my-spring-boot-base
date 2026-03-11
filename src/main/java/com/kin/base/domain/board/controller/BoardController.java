package com.kin.base.domain.board.controller;

import com.kin.base.domain.board.dto.BoardDto;
import com.kin.base.domain.board.dto.BoardSearchCondition;
import com.kin.base.domain.board.entity.BoardFile;
import com.kin.base.domain.board.repository.BoardRepository;
import com.kin.base.domain.board.service.BoardService;
import com.kin.base.domain.member.dto.MemberLoginDto;
import com.kin.base.global.common.FileStore;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Tag(name = "Board", description = "게시판 관련 API") // 컨트롤러 그룹화
@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;
    private final FileStore fileStore;

    @GetMapping("/boardList")
    public String boardList(@PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable // 페이징 정보 (스프링 제공)
            , @ModelAttribute BoardSearchCondition boardSearchCondition
            , @SessionAttribute(name = "member", required = false) MemberLoginDto loginMember // 세션 정보 주입
            , Model model) {

        if(loginMember != null) {
            log.info("-=-로그인한 사용자 : " + loginMember.getLoginId());
            model.addAttribute("member", loginMember);
        }

        Page<BoardDto> boardList = boardService.findAll(pageable, boardSearchCondition);
        model.addAttribute("boardList", boardList);

        return "board/BoardList";
    }

    @GetMapping("/{id}")
    public String boardDetail(@PathVariable Long id
            , @SessionAttribute(name = "member", required = false) MemberLoginDto loginMember
            , RedirectAttributes redirectAttributes
            , Model model) {

        try {

            model.addAttribute("board", boardService.findById(id));
            model.addAttribute("member", loginMember);

            return "board/BoardDetail";
        } catch (Exception e) {
            log.info("-=-게시글 상세조회 실패! e.getMessage() : " + e.getMessage());
            model.addAttribute("errorMessage",e.getMessage());

            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/board/boardList";
        }

    }

    @GetMapping("/new")
    public String newBoard(@SessionAttribute(name = "member", required = false) MemberLoginDto loginMember
                           , Model model) {

        BoardDto boardDto = new BoardDto();

        if(loginMember != null) {
            //model.addAttribute("member", loginMember);
            boardDto.setAuthor(loginMember.getLoginId());
        }

        model.addAttribute("formMode", "new");
        model.addAttribute("board", boardDto);

        return "board/boardForm";
    }

    //첨부파일 없는 버전
    /*@PostMapping("/save")
    public String save(@Valid @ModelAttribute("board") BoardDto boardDto
            , BindingResult bindingResult){

        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "board/boardForm"; // 에러가 있으면 다시 작성 화면으로
        }

        Long id = boardService.save(boardDto);

        return "redirect:/board/"+id;
    }*/

    //첨부파일 있는 버전
    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("board") BoardDto boardDto
            , BindingResult bindingResult
            , @RequestParam("boardFiles") List<MultipartFile> boardFiles) throws IOException {

        // 1. 파일 개수 검증 (공백 파일 제외 로직 포함)
        if (boardFiles != null && boardFiles.size() > 3) {
            // 특정 필드(boardFiles)에 대한 에러 메시지 등록
            bindingResult.rejectValue("boardFiles", "maxFiles", "첨부파일은 최대 3개까지만 업로드 가능합니다.");
        }

        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "board/boardForm"; // 에러가 있으면 다시 작성 화면으로
        }

        Long id = boardService.save(boardDto, boardFiles);

        return "redirect:/board/"+id;
    }

    @GetMapping("/edit/{id}")
    public String boardEditForm(@PathVariable Long id
            , @SessionAttribute(name = "member", required = false) MemberLoginDto loginMember
            , RedirectAttributes redirectAttributes
            , Model model) {

        BoardDto boardDto = boardService.findById(id);

        if(loginMember.getLoginId().equals(boardDto.getAuthor())) {
            model.addAttribute("formMode", "update");
            model.addAttribute("board", boardDto);
            model.addAttribute("member", loginMember);

            return "board/boardForm";
        }else{
            redirectAttributes.addFlashAttribute("errorMessage", "잘못된 접근입니다.");
            return "redirect:/board/boardList";
        }


    }

    @PostMapping("/update/{id}")
    public String updateBoard(@Valid @ModelAttribute("board") BoardDto boardDto
            , BindingResult bindingResult
            ,Model model){

        if(bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "board/boardForm";
        }

        return "redirect:/board/"+boardService.update(boardDto);

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteBoard(@PathVariable Long id) {

        boardService.delete(id);

        return ResponseEntity.ok("ok");

    }

    // 이미지 출력용 (Resource)
    @ResponseBody
    @GetMapping("/images/{filename}")
    public Resource showImage(@PathVariable String filename) throws MalformedURLException {
        // FileStore에서 설정한 경로의 파일을 읽어옴
        return new UrlResource("file:" + fileStore.getFullPath(filename));
    }

    // 파일 다운로드용
    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id) throws MalformedURLException {
        BoardFile boardFile = boardService.findFileById(id); // 파일 정보 조회
        String storeFileName = boardFile.getSaveName();
        String uploadFileName = boardFile.getOriginName();

        UrlResource resource = new UrlResource("file:" + fileStore.getFullPath(storeFileName));

        // 한글 파일명 깨짐 방지
        String encodedUploadFileName = UriUtils.encode(uploadFileName, StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=\"" + encodedUploadFileName + "\"";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(resource);
    }

}
