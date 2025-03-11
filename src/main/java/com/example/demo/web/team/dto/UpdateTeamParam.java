package com.example.demo.web.team.dto;

import com.example.demo.web.team.entity.Team;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTeamParam {

    private String name;
    private String rank;
    private Team upperTeam;
    private Long upperTeamId;
}
