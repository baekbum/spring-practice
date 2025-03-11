package com.example.demo.web.team.service;

import com.example.demo.web.common.aspect.annotation.TimeAop;
import com.example.demo.web.team.dto.InsertTeamParam;
import com.example.demo.web.team.dto.TeamCondition;
import com.example.demo.web.team.dto.TeamDto;
import com.example.demo.web.team.dto.UpdateTeamParam;
import com.example.demo.web.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository repository;

    @TimeAop
    public TeamDto addTeam(InsertTeamParam param) {
        return repository.addTeam(param);
    }

    @TimeAop
    @Transactional(readOnly = true)
    public TeamDto findTeam(long id) {
        return repository.findTeam(id);
    }

    @TimeAop
    @Transactional(readOnly = true)
    public List<TeamDto> findTeams(TeamCondition condition) {
        return repository.findTeams(condition);
    }

    @TimeAop
    public TeamDto updateTeam(long id, UpdateTeamParam param) {
        return repository.updateTeam(id, param);
    }

    @TimeAop
    public TeamDto deleteTeam(long id) {
        return repository.deleteTeam(id);
    }

}
