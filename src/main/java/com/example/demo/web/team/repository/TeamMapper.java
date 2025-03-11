package com.example.demo.web.team.repository;

import com.example.demo.web.team.dto.TeamCondition;
import com.example.demo.web.team.dto.TeamDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TeamMapper {

    void save(TeamDto dto);

    TeamDto findTeam(long id);

    List<TeamDto> findTeams(TeamCondition condition);

    //void update(@Param("id") long id, @Param("dto") TeamDto dto);
    void update(TeamDto dto);

    void delete(long id);
}
