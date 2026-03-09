package com.kin.base.domain.defaultBoard.service;

import com.kin.base.domain.defaultBoard.dto.DefaultBoardDto;
import com.kin.base.domain.defaultBoard.dto.DefaultBoardSearchCondition;
import com.kin.base.domain.defaultBoard.entity.DefaultBoard;
import com.kin.base.domain.defaultBoard.repository.DefaultBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DefaultBoardService {

    private final DefaultBoardRepository defaultBoardRepository;

    public Page<DefaultBoardDto> findAll(Pageable pageable, DefaultBoardSearchCondition defaultBoardSearchCondition) {

        // 0번째 페이지부터 10개씩, ID 역순으로 정렬
        //Pageable pageable = PageRequest.of(page, 10, Sort.by("id").descending());

        //Page<DefaultBoard> entityList = defaultBoardRepository.findAll(pageable);

        //문자열일 경우 날짜를 처리하는 방법
        LocalDateTime searchStartDate = null;
        LocalDateTime searchEndDate = null;

        if (StringUtils.hasText(defaultBoardSearchCondition.getCreatedDate())) {
            // "2026-03-19" 문자열을 해당 날짜의 0시 0분으로 변환
            searchStartDate = LocalDate.parse(defaultBoardSearchCondition.getCreatedDate()).atStartOfDay();
            searchEndDate = LocalDate.parse(defaultBoardSearchCondition.getCreatedDate()).atTime(LocalTime.MAX);
        }

        // @Query
        /*Page<DefaultBoard> entityList = defaultBoardRepository.search(defaultBoardSearchCondition.getTitle(),
                defaultBoardSearchCondition.getAuthor(),
                searchStartDate,
                searchEndDate,
                pageable);*/

        Page<DefaultBoard> entityList = defaultBoardRepository.searchQuerydsl(defaultBoardSearchCondition.getTitle(),
                defaultBoardSearchCondition.getAuthor(),
                searchStartDate,
                searchEndDate,
                pageable);

        return entityList.map(DefaultBoardDto::new);

        //return entityList.map(entity -> new DefaultBoardDto(entity));

        //List<DefaultBoard> entityList = defaultBoardRepository.findAll();
        /*return entityList.stream()
                .map(entity -> new DefaultBoardDto(entity))
                .collect(Collectors.toList());*/

    }

    public DefaultBoardDto findById(Long id) {
        DefaultBoard defaultBoard = defaultBoardRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        return new DefaultBoardDto(defaultBoard);
    }

    public Long save(DefaultBoardDto defaultBoardDto) {

        DefaultBoard defaultBoard = DefaultBoard.builder()
                .content(defaultBoardDto.getContent())
                .title(defaultBoardDto.getTitle())
                .author(defaultBoardDto.getAuthor())
                .build();

        return defaultBoardRepository.save(defaultBoard).getId();

    }

    public Long update(DefaultBoardDto defaultBoardDto) {

        DefaultBoard defaultBoard = DefaultBoard.builder()
                .id(defaultBoardDto.getId())
                .content(defaultBoardDto.getContent())
                .title(defaultBoardDto.getTitle())
                .author(defaultBoardDto.getAuthor())
                .build();

        return defaultBoardRepository.save(defaultBoard).getId();

    }

    public void deleteById(Long id) {

        defaultBoardRepository.deleteById(id);

    }

}
