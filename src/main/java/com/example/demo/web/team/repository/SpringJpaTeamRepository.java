package com.example.demo.web.team.repository;

import com.example.demo.web.team.common.TeamRank;
import com.example.demo.web.team.dto.InsertTeamParam;
import com.example.demo.web.team.dto.TeamCondition;
import com.example.demo.web.team.dto.TeamDto;
import com.example.demo.web.team.dto.UpdateTeamParam;
import com.example.demo.web.team.entity.Team;
import com.example.demo.web.team.exception.NoSearchTeamException;
import com.example.demo.web.team.exception.TeamDuplicationException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Primary
@Repository
@RequiredArgsConstructor
public class SpringJpaTeamRepository implements TeamRepository {

    private final SpringJpaTeam repository;
    private final EntityManager em;

    @Override
    public TeamDto addTeam(InsertTeamParam param) {
        Team upperTeam = findUpperTeam(param.getUpperTeamId());

        duplicateCheck(param, upperTeam);

        Team newTeam = new Team(param, upperTeam);
        repository.save(newTeam);

        return new TeamDto(newTeam);
    }

    public Team findUpperTeam(long upperTeamId) {
        Optional<Team> findUpperTeam = repository.findById(upperTeamId);
        return findUpperTeam.orElseThrow(() -> new NoSearchTeamException("상위 팀을 찾을 수 없습니다."));
    }

    public void duplicateCheck(InsertTeamParam param, Team upperTeam) {
        Optional<Team> findTeam = repository.findByNameAndRankAndUpperTeam(param.getName(), TeamRank.valueOf(param.getRank()), upperTeam);
        if (findTeam.isPresent()) throw new TeamDuplicationException("이미 해당 팀이 존재합니다.");
    }

    @Override
    public TeamDto findTeam(Long id) {
        Team team = repository.findById(id)
                .orElseThrow(() -> new NoSearchTeamException("해당 팀을 찾을 수 없습니다."));

        return new TeamDto(team);
    }

    @Override
    public List<TeamDto> findTeams(TeamCondition condition) {
        StringBuilder jpql = new StringBuilder("SELECT t FROM Team t WHERE 1=1");
        Map<String, Object> paramMaps = new HashMap<>();

        if (condition.getId() != null) {
            jpql.append(" AND t.id = :id");
            paramMaps.put("id", condition.getId());
        }

        if (condition.getName() != null) {
            jpql.append(" AND t.name LIKE :name");
            paramMaps.put("name", "%" + condition.getName() + "%");
        }

        if (condition.getRank() != null) {
            jpql.append(" AND t.rank = :rank");
            paramMaps.put("rank", TeamRank.valueOf(condition.getRank()));
        }

        if (condition.getUpperTeamId() != null) {
            Team upperTeam = findUpperTeam(condition.getUpperTeamId());
            jpql.append(" AND t.upperTeam = :upperTeam");
            paramMaps.put("upperTeam", upperTeam);
        }

        TypedQuery<Team> query = em.createQuery(jpql.toString(), Team.class);

        Set<String> keys = paramMaps.keySet();
        for (String key : keys) {
            query.setParameter(key, paramMaps.get(key));
        }

        List<Team> result = query.getResultList();

        List<TeamDto> findTeams = new ArrayList<>();

        if (!result.isEmpty()) {
            findTeams = result.stream()
                    .map(TeamDto::new)
                    .collect(Collectors.toList());
        }

        return findTeams;
    }

    @Override
    public TeamDto updateTeam(long id, UpdateTeamParam param) {
        Team upperTeam = findUpperTeam(param.getUpperTeamId());

        Team findTeam = repository.findById(id).orElseThrow(() -> new NoSearchTeamException("해당 팀을 찾을 수 없습니다."));

        if (param.getUpperTeamId() != null) {
            param.setUpperTeam(repository.findById(param.getUpperTeamId()).get());
        }

        findTeam.updateTeam(param);

        return new TeamDto(findTeam);
    }

    @Override
    public TeamDto deleteTeam(long id) {
        Team findTeam = repository.findById(id).orElseThrow(() -> new NoSearchTeamException("해당 팀을 찾을 수 없습니다."));

        repository.delete(findTeam);

        return new TeamDto(findTeam);
    }
}
