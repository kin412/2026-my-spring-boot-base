package com.kin.base.domain.board.controller;

import com.kin.base.domain.board.repository.BoardRepository;
import com.kin.base.domain.board.service.BoardService;
import com.kin.base.domain.defaultBoard.repository.DefaultBoardRepository;
import com.kin.base.domain.member.dto.MemberLoginDto;
import com.kin.base.domain.member.repository.MemberRepository;
import com.kin.base.global.common.FileStore;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@SpringBootTest // 실제 빈을 다 띄우기때문에 무겁고 오래걸림. 그리고 mockbean을 쓰지 않으면 실db에 까지 붙음
//@AutoConfigureMockMvc // @SpringBootTest 사용 시 이걸 붙여야 MockMvc 주입이 가능

// BoardController 관련 빈들만 로드
/*시작시 설정정보를 찾기위해 @SpringBootApplication을 찾아서 시작함
근데 @SpringBootApplication에 지금 다른 의존성 주입을 받는 객체들이 있는데
WebMvcTest는 스프링컨테이너를 다띄우는게 아니라 web 레이어 하나만띄움 - 웹과 관련된 최소한의 빈들만으로 컨테이너를 띄움
@Controller, @ControllerAdvice, Filter, HandlerMethodArgumentResolver 등.

@Service, @Repository, @Component 이런 빈들은 포함되지 않으므로
해당 빈을 주입받는 객체들을 @MockitoBean 으로  빈을 해당 인터페이스나 클래스의 껍데기만 따서 가짜객체를 만들어 주입해줌

*/
@WebMvcTest(BoardController.class)
class BoardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    //@MockBean //스프링부트 3.4부터 @MockBean과 @SpyBean이 @MockitoBean과 @MockitoSpyBean으로 교체됨
    @MockitoBean
    private BoardService boardService;

    @MockitoBean
    private FileStore fileStore;

    @MockitoBean
    private BoardRepository boardRepository;

    @MockitoBean
    private MemberRepository memberRepository;
    
    //baseapplication에서 @EnableJpaAuditing // 이 설정이 있어야 자동으로 날짜가 찍힘
    //EnableJpaAuditing을 별도의 설정파일로 분리

    @Test
    @DisplayName("파일을 포함한 게시글 저장 테스트")
    void saveBoardWithFiles() throws Exception {
        // Given: 가짜 파일과 세션 데이터 준비
        MockMultipartFile file = new MockMultipartFile(
                "boardFiles", "test.txt", "text/plain", "test data".getBytes());

        MemberLoginDto loginMember = new MemberLoginDto();
        loginMember.setLoginId("user1");

        // When & Then
        mockMvc.perform(multipart("/board/save") // multipart 요청
                        .file(file)
                        .param("title", "테스트타이틀testTitle")
                        .param("content", "테스트컨텐츠testContent")
                        .param("author", loginMember.getLoginId())
                        .sessionAttr("member", loginMember)) // 세션 주입
                .andExpect(status().is3xxRedirection()) // 저장 후 리다이렉트 확인
                .andExpect(view().name(startsWith("redirect:/board/")));

        // 컨트롤러 로직 안에서 boardService의 save 메서드가 실제로 호출되었는지 검증
        verify(boardService).save(any(), any());

    }

    @Test
    @DisplayName("파일 개수 초과 시 에러 메시지 확인")
    void fileLimitTest() throws Exception {
        // Given: 파일 4개 준비 (컨트롤러 로직상 3개 제한)
        MockMultipartFile file = new MockMultipartFile("boardFiles", "test.txt", "text/plain", "data".getBytes());

        mockMvc.perform(multipart("/board/save")
                        .file(file).file(file).file(file).file(file)
                        .param("title", "제목")
                        .param("content", "내용"))
                .andExpect(model().attributeHasErrors("board")) // 에러 발생 확인
                .andExpect(view().name("board/boardForm"));
    }


}