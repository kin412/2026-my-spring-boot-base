package com.kin.base.domain.defaultBoard.service;

import com.kin.base.domain.defaultBoard.dto.DefaultBoardDto;
import com.kin.base.domain.defaultBoard.entity.DefaultBoard;
import com.kin.base.domain.defaultBoard.repository.DefaultBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DefaultBoardService {

    private final DefaultBoardRepository defaultBoardRepository;

    public List<DefaultBoardDto> findAll() {
        List<DefaultBoard> entityList = defaultBoardRepository.findAll();
        return entityList.stream()
                .map(entity -> new DefaultBoardDto(entity))
                .collect(Collectors.toList());
    }

}
