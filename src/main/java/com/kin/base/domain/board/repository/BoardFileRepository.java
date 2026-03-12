package com.kin.base.domain.board.repository;

import com.kin.base.domain.board.entity.BoardFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BoardFileRepository extends JpaRepository<BoardFile, Long> {

    //Optional<BoardFile> findByIdWithFiles(@Param("id") Long id);
    List<BoardFile> findByBoardIdIn(List<Long> boardIds);
}
