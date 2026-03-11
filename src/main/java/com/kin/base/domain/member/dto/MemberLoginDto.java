package com.kin.base.domain.member.dto;

import com.kin.base.domain.member.entity.Member;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberLoginDto {

    @NotBlank(message = "아이디는 필수 입력입니다.")
    @Size(max = 50, message = "아이디는 50자 이내여야 합니다.")
    private String loginId;
    @NotBlank(message = "비밀번호는 필수 입력입니다.")
    @Size(max = 100, message = "비밀번호는 100자 이내여야 합니다.")
    private String pw;

    @Builder
    public MemberLoginDto(Member member) {
        this.loginId = member.getLoginId();

    }

}
