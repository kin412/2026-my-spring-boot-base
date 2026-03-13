package com.kin.base;

import com.kin.base.domain.board.entity.Board;
import com.kin.base.domain.board.repository.BoardRepository;
import com.kin.base.domain.member.entity.Member;
import com.kin.base.domain.member.repository.MemberRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
//@EnableJpaAuditing // 이 설정이 있어야 자동으로 날짜가 찍힘 //여기있으면 테스트가 안됨. 별도의 설정파일로 분리
@SpringBootApplication
public class BaseApplication {

	//@Autowired
	//private DefaultBoardRepository defaultBoardRepository;

	@Autowired
	private BoardRepository boardRepository;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private InitService initService;

	public static void main(String[] args) {
		SpringApplication.run(BaseApplication.class, args);
	}

	@PostConstruct
	public void init() {
		//테스트데이터 생성.
		log.info("-=-BaseApplication init");

		initService.dbInit();

		/*createAdmin();
		createTestMember();

		Member member = memberRepository.login("admin", "1111").get();

		for(int i = 1; i <= 100; i++) {
			Board board = Board.builder()
					.type("board")
					.title("테스트타이틀" + i)
					.content("테스트내용" + i)
					.build();

			board.changeAuthor(member);

			boardRepository.save(board);
		}*/


	}

	@Component
	@Transactional // 👈 여기서 트랜잭션을 묶어줘야 영속성 컨텍스트가 유지됩니다.
	@RequiredArgsConstructor
	static class InitService {
		private final MemberRepository memberRepository;
		private final BoardRepository boardRepository;

		public void dbInit() {
			// 1. 관리자 생성
			Member admin = Member.builder()
					.loginId("admin").pw("1111").name("testAdmin")
					.gender("M").email("asd123@naver.com").age(30).address("강남구")
					.build();
			memberRepository.save(admin);

			// 2. 테스트 멤버 생성
			Member user = Member.builder()
					.loginId("1").pw("1").name("testMember")
					.gender("Y").email("sfsdfewr@gmail.com").age(23).address("부산")
					.build();
			memberRepository.save(user);

			// 3. 게시글 생성 (이제 admin은 같은 트랜잭션 안이라 '영속 상태'입니다)
			for(int i = 1; i <= 100; i++) {
				Board board = Board.builder()
						.type("BOARD")
						.title("테스트타이틀" + i)
						.content("테스트내용" + i)
						.build();

				board.changeAuthor(admin); // 지연 로딩 에러 없이 정상 작동
				boardRepository.save(board);
			}
		}
	}




	public void createAdmin(){
		//로그인 테스트id
		log.info("-=-login Test Id");
		Member member = Member.builder()
				.loginId("admin")
				.pw("1111")
				.name("testAdmin")
				.gender("M")
				.email("asd123@naver.com")
				.age(30)
				.address("강남구 테헤란로")
				.build();

		memberRepository.save(member);
	}

	public void createTestMember(){
		//로그인 테스트id
		log.info("-=-login Test Id");
		Member member = Member.builder()
				.loginId("1")
				.pw("1")
				.name("testMember")
				.gender("Y")
				.email("sfsdfewr@gmail.com")
				.age(23)
				.address("부산광역시")
				.build();

		memberRepository.save(member);
	}

}
