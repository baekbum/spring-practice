package com.example.demo.web.team.repository;

import com.example.demo.web.team.dto.InsertTeamParam;
import com.example.demo.web.team.dto.TeamCondition;
import com.example.demo.web.team.dto.TeamDto;
import com.example.demo.web.team.dto.UpdateTeamParam;
import com.example.demo.web.team.entity.Team;
import com.example.demo.web.team.exception.NoSearchTeamException;
import com.example.demo.web.team.exception.TeamDuplicationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
//@Primary
@Repository
@RequiredArgsConstructor
public class MyBatisTeamRepository {
//public class MyBatisTeamRepository implements TeamRepository {

    /*
    private final TeamMapper teamMapper;

    @Override
    public TeamDto addTeam(InsertTeamParam param) {

        duplicationCheck(param);

        TeamDto teamDto = new TeamDto(param);
        teamMapper.save(teamDto);

        teamDto.setMessage(teamDto.getName() + "을(를) 생성 하였습니다.");
        return teamDto;
    }

    private void duplicationCheck(InsertTeamParam param) {
        // 중복되는 팀이 있는지 확인하는 절차
        List<TeamDto> findTeam = findTeams(new TeamCondition(param.getName(), param.getRank(), param.getUpperTeamId()));
        if (!findTeam.isEmpty() && !param.getRank().equals("HEADQUARTER")) throw new TeamDuplicationException("이미 해당 팀이 존재합니다.");
    }

    @Override
    public TeamDto findTeam(Long id) {
        TeamDto teamDto = teamMapper.findTeam(id);

        if (teamDto == null) throw new NoSearchTeamException("해당 팀을 찾을 수 없습니다.");

        return teamDto;
    }

    @Override
    public List<TeamDto> findTeams(TeamCondition condition) {
        return teamMapper.findTeams(condition);
    }

    @Override
    public TeamDto updateTeam(long id, UpdateTeamParam param) {
        TeamDto teamDto = new TeamDto(id, param);
        teamMapper.update(teamDto);

        teamDto.setMessage(teamDto.getName() + "을(를) 수정 하였습니다.");

        return teamDto;
    }

    @Override
    public TeamDto deleteTeam(long id) {
        TeamDto teamDto = findTeam(id);

        teamMapper.delete(id);

        teamDto.setMessage(teamDto.getName() + "을(를) 삭제 하였습니다.");

        return teamDto;
    }

     */
}
