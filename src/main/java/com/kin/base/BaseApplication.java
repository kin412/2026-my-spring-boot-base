package com.kin.base;

import com.kin.base.domain.defaultBoard.entity.DefaultBoard;
import com.kin.base.domain.defaultBoard.repository.DefaultBoardRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Slf4j
@EnableJpaAuditing // 이 설정이 있어야 자동으로 날짜가 찍힘
@SpringBootApplication
public class BaseApplication {

	@Autowired
	private DefaultBoardRepository defaultBoardRepository;

	public static void main(String[] args) {
		SpringApplication.run(BaseApplication.class, args);
	}

	@PostConstruct
	public void init() {
		//테스트데이터 생성.
		log.info("-=-BaseApplication init");

		for(int i = 1; i <= 100; i++) {
			DefaultBoard defaultBoard = DefaultBoard.builder()
					.content("테스트내용" + i)
					.title("테스트타이틀" + i)
					.author("테스트작성자" + i)
					.build();
			defaultBoardRepository.save(defaultBoard);
		}
	}

}
