package com.kin.base.domain.board.repository;

import com.kin.base.domain.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardRepositoryCustom {

    //간단한 경우엔 @entitygraph
    //@EntityGraph(attributePaths = {"author"}) // 연관된 author를 한 번에 가져오라!
    @Query("select b from Board b join fetch b.author where b.id = :id")
    Optional<Board> findByIdWithAuthor(@Param("id") Long id);

}
