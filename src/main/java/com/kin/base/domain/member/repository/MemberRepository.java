package com.kin.base.domain.member.repository;

import com.kin.base.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

    @Query("select m from Member m "+
    "where m.loginId = :loginId and m.pw = :pw")
    Optional<Member> login(String loginId, String pw);

    Optional<Member> findByLoginId(String loginId);

}
