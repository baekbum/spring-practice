package com.example.demo.web.team.repository;

import com.example.demo.web.team.dto.InsertTeamParam;
import com.example.demo.web.team.dto.TeamCondition;
import com.example.demo.web.team.dto.TeamDto;
import com.example.demo.web.team.dto.UpdateTeamParam;
import com.example.demo.web.team.entity.Team;

import java.util.List;

public interface TeamRepository {

    TeamDto addTeam(InsertTeamParam param);

    TeamDto findTeam(Long id);

    List<TeamDto> findTeams(TeamCondition condition);

    TeamDto updateTeam(long id, UpdateTeamParam param);

    TeamDto deleteTeam(long id);

}
