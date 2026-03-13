package com.kin.base.domain.board.service;

import com.kin.base.domain.board.dto.BoardDto;
import com.kin.base.domain.board.repository.BoardRepository;
import com.kin.base.domain.member.repository.MemberRepository;
import com.kin.base.global.common.FileStore;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class BoardServiceTest {

    @InjectMocks
    private BoardService boardService;

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private FileStore fileStore;

    @Test
    @DisplayName("게시글 저장 시 작성자가 없으면 예외가 발생한다")
    void save_fail_member_not_found() {
        // given
        BoardDto dto = new BoardDto();
        dto.setAuthor("wrong_id");

        // memberRepository.findByLoginId가 빈 Optional을 반환하도록 설정
        given(memberRepository.findByLoginId(any())).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> boardService.save(dto, Collections.emptyList()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("잘못된 사용자 ID");
    }
}