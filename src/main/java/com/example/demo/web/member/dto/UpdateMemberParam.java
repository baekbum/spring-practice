package com.example.demo.web.member.dto;

import com.example.demo.web.team.entity.Team;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMemberParam {

    private String password;
    private String name;
    private String brith;
    private String rank;
    private Team team;
    private Long teamId;

    public void encodePassword(PasswordEncoder encoder) {
        this.password = encoder.encode(this.getPassword());
    }
}
