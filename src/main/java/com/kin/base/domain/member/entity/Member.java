package com.kin.base.domain.member.entity;


import com.kin.base.domain.board.entity.Board;
import com.kin.base.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "MEMBER")
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "ID") // 시스템 관리용 PK (NUMBER)
    private Long id;

    @Column(name = "LOGIN_ID", unique = true, nullable = false, length = 50)
    private String loginId;

    @Column(name = "PW", nullable = false, length = 100) // 암호화 대비 여유있게
    private String pw;

    @Column(name = "NAME", nullable = false, length = 20)
    private String name;

    @Column(name = "AGE", nullable = false)
    private int age;

    @Column(name = "GENDER", nullable = false, length = 1) // CHAR(1) 대응
    private String gender;

    @Column(name = "ADDRESS", nullable = false, length = 500)
    private String address;

    @Column(name = "EMAIL", nullable = false, length = 500)
    private String email;

    // 양방향 연관관계: Board의 author 필드에 의해 관리됨
    @OneToMany(mappedBy = "author")
    private List<Board> boards = new ArrayList<>();

    @Builder
    public Member(String loginId, String pw, String name, String gender, String email, int age, String address) {
        this.loginId = loginId;
        this.pw = pw;
        this.name = name;
        this.gender = gender;
        this.email = email;
        this.age = age;
        this.address = address;
    }
}
