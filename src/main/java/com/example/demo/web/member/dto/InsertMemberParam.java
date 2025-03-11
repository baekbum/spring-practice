package com.example.demo.web.member.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class InsertMemberParam {

    @NotEmpty(message = "ID는 필수 입니다.")
    private String id;
    @NotEmpty(message = "비밀번호는 필수 입니다.")
    private String password;
    @NotEmpty(message = "이름은 필수 입니다.")
    private String name;
    private String brith;
    @NotEmpty(message = "직급은 필수 입니다.")
    private String rank;
    @NotNull(message = "소속 팀은 필수 입니다.")
    private Long teamId;

    public void encodePassword(PasswordEncoder encoder) {
        this.password = encoder.encode(this.getPassword());
    }
}
