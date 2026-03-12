package com.kin.base.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    // DB 제약 조건 위반 (아까 발생한 외래키 에러 등) 처리
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDataIntegrityViolation(DataIntegrityViolationException e) {
        log.error("Database constraint violation: {}", e.getMessage());

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.CONFLICT.value());
        body.put("error", "데이터 무결성 위반");
        body.put("message", "관련된 데이터(파일 등)가 있어 삭제할 수 없거나 데이터 규칙에 어긋납니다.");

        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }

    // 일반적인 모든 예외 처리
    //api - responseEntity
    /*@ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleAllExceptions(Exception e) {
        log.error("Unhandled exception occurred: ", e);

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put("error", "서버 내부 오류");
        body.put("message", "알 수 없는 오류가 발생했습니다. 관리자에게 문의하세요.");

        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }*/

    @ExceptionHandler(Exception.class)
    public String handleAllExceptions(Exception e, Model model) {
        log.error("Unhandled exception occurred: ", e);

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put("error", "서버 내부 오류");
        body.put("message", "알 수 없는 오류가 발생했습니다. 관리자에게 문의하세요.");

        model.addAttribute("body", body);

        return "global/exception/allException";
    }

}
