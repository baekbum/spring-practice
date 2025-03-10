package com.example.demo.web.team.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TeamCondition {

    private Long id;
    private String name;
    private String rank;
    private Long upperTeamId;

    public TeamCondition(String name, String rank, Long upperTeamId) {
        this.name = name;
        this.rank = rank;
        this.upperTeamId = upperTeamId;
    }
}
