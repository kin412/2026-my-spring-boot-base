package com.kin.base.domain.defaultBoard.repository;

import com.kin.base.domain.defaultBoard.entity.DefaultBoard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DefaultBoardRepository extends JpaRepository<DefaultBoard, Long> {
    // 기본적인 save, findById, findAll, delete 등은 이미 내장되어 있음.
}
