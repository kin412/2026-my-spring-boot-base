package com.kin.base.domain.defaultBoard.repository;

import com.kin.base.domain.defaultBoard.entity.DefaultBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface DefaultBoardRepository extends JpaRepository<DefaultBoard, Long>, DefaultBoardRepositoryCustom {
    // 기본적인 save, findById, findAll, delete 등은 이미 내장되어 있음.

    @Query("select b from DefaultBoard b " +
            "where (:title is null or b.title like %:title%) " +
            "and (:author is null or b.author like %:author%) " +
            "and (:start is null or b.createdDate between :start and :end)")
    Page<DefaultBoard> search(String title, String author, LocalDateTime start, LocalDateTime end, Pageable pageable);

}
