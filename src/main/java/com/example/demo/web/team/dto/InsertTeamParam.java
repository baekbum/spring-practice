package com.example.demo.web.team.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class InsertTeamParam {

    @NotEmpty(message = "팀명은 필수 입니다.")
    private String name;
    @NotEmpty(message = "팀 분류는 필수 입니다.")
    private String rank;
    private Long upperTeamId;
}
