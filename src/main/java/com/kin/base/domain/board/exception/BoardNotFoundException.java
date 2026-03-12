package com.kin.base.domain.board.exception;

/**
 * 게시글을 찾을 수 없을 때 던지는 비즈니스 예외
 */
public class BoardNotFoundException extends RuntimeException {

    public BoardNotFoundException(Long id) {
        super("해당 게시글을 찾을 수 없습니다. (ID: " + id + ")");
    }

    public BoardNotFoundException(String message) {
        super(message);
    }
}
