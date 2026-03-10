package com.kin.base.domain.board.repository;

import com.kin.base.domain.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardRepositoryCustom {
}
