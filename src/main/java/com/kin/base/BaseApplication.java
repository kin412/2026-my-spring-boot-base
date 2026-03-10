package com.kin.base;

import com.kin.base.domain.board.entity.Board;
import com.kin.base.domain.board.repository.BoardRepository;
import com.kin.base.domain.defaultBoard.entity.DefaultBoard;
import com.kin.base.domain.defaultBoard.repository.DefaultBoardRepository;
import com.kin.base.domain.member.entity.Member;
import com.kin.base.domain.member.repository.MemberRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@Slf4j
@EnableJpaAuditing // 이 설정이 있어야 자동으로 날짜가 찍힘
@SpringBootApplication
public class BaseApplication {

	@Autowired
	private DefaultBoardRepository defaultBoardRepository;

	@Autowired
	private BoardRepository boardRepository;

	@Autowired
	private MemberRepository memberRepository;

	public static void main(String[] args) {
		SpringApplication.run(BaseApplication.class, args);
	}

	@PostConstruct
	public void init() {
		//테스트데이터 생성.
		log.info("-=-BaseApplication init");

		createAdmin();

		Member member = memberRepository.login("admin", "1111").get();

		for(int i = 1; i <= 100; i++) {
			Board board = Board.builder()
					.type("board")
					.title("테스트타이틀" + i)
					.content("테스트내용" + i)
					.author(member)
					.build();
			boardRepository.save(board);
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

}
