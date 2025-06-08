package com.example.demo.web.team.repository;

import com.example.demo.web.team.dto.InsertTeamParam;
import com.example.demo.web.team.dto.TeamCondition;
import com.example.demo.web.team.dto.TeamDto;
import com.example.demo.web.team.dto.UpdateTeamParam;
import com.example.demo.web.team.entity.Team;

import java.util.List;

public interface TeamRepository {

    Team addTeam(InsertTeamParam param);

    Team findTeam(Long id);

    List<Team> findTeams(TeamCondition condition);

    Team updateTeam(long id, UpdateTeamParam param);

    Team deleteTeam(long id);

}
