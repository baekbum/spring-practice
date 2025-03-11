package com.example.demo.web.team.dto;

import com.example.demo.web.common.dto.ResponseDto;
import com.example.demo.web.team.entity.Team;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class TeamDto extends ResponseDto {

    private long id;
    private String name;
    private String rank;
    private Long upperTeamId;
    private String upperTeamName;

    public TeamDto(InsertTeamParam param) {
        this.name = param.getName();
        this.rank = param.getRank();
        this.upperTeamId = param.getUpperTeamId();
    }

    public TeamDto(long id, UpdateTeamParam param) {
        this.id = id;
        this.name = param.getName();
        this.rank = param.getRank();
        this.upperTeamId = param.getUpperTeamId();
    }

    public TeamDto(Team team) {
        this.id = team.getId();
        this.name = team.getName();
        this.rank = team.getRank().name();
        this.upperTeamId = (team.getUpperTeam() != null ) ? team.getUpperTeam().getId() : null;
        this.upperTeamName = (team.getUpperTeam() != null ) ? team.getUpperTeam().getName() : null;
    }
}
