package com.example.demo.web.team.entity;

import com.example.demo.web.team.common.TeamRank;
import com.example.demo.web.team.dto.InsertTeamParam;
import com.example.demo.web.team.dto.UpdateTeamParam;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TEAM_TABLE_SEQUENCE_GENERATOR")
    @SequenceGenerator(name = "TEAM_TABLE_SEQUENCE_GENERATOR", sequenceName = "TEAM_TABLE_SEQUENCE", allocationSize = 1)
    private long id;
    private String name;

    @Enumerated(EnumType.STRING)
    private TeamRank rank;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "upper_team_id")
    private Team upperTeam;

    public Team(InsertTeamParam param, Team upperTeam) {
        this.name = param.getName();
        this.rank = TeamRank.valueOf(param.getRank());
        this.upperTeam = upperTeam;
    }

    public void updateTeam(UpdateTeamParam param) {
        this.name = param.getName() != null ? param.getName() : this.name;
        this.rank = param.getRank() != null ? TeamRank.valueOf(param.getRank()) : this.rank;
        this.upperTeam = param.getUpperTeam() != null ? param.getUpperTeam() : this.upperTeam;
    }
}
