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
import jakarta.persistence.Query;
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
public class JpaTeamRepository implements TeamRepository {

    private final EntityManager em;

    @Override
    public TeamDto addTeam(InsertTeamParam param) {
        Team upperTeam = findUpperTeam(param.getUpperTeamId());

        duplicateCheck(param, upperTeam);

        Team newTeam = new Team(param, upperTeam);
        em.persist(newTeam);

        return new TeamDto(newTeam);
    }

    /**
     * 상위팀을 찾는 메서드
     * @param upperTeamId
     * @return
     */
    private Team findUpperTeam(long upperTeamId) {
        return em.find(Team.class, upperTeamId);
    }

    /**
     * 중복되는 팀이 있는지 확인하는 메서드
     * @param param
     */
    private void duplicateCheck(InsertTeamParam param, Team upperTeam) {
        String jpql = "SELECT t FROM Team t WHERE t.name = :name AND t.rank = :rank AND t.upperTeam = :upperTeam";

        Query query = em.createQuery(jpql, Team.class);
        query.setParameter("name", param.getName());
        query.setParameter("rank", TeamRank.valueOf(param.getRank()));
        query.setParameter("upperTeam", upperTeam);

        List<Team> resultList = query.getResultList();

        if (!resultList.isEmpty()) throw new TeamDuplicationException("이미 해당 팀이 존재합니다.");
    }

    @Override
    public TeamDto findTeam(Long id) {
        Team findTeam = em.find(Team.class, id);

        if (findTeam == null) throw new NoSearchTeamException("해당 팀을 찾을 수 없습니다.");

        return new TeamDto(findTeam);
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
        Team findTeam = em.find(Team.class, id);

        if (param.getUpperTeamId() != null) {
            param.setUpperTeam(em.find(Team.class, param.getUpperTeamId()));
        }

        findTeam.updateTeam(param);

        return new TeamDto(findTeam);
    }

    @Override
    public TeamDto deleteTeam(long id) {
        Team findTeam = em.find(Team.class, id);
        em.remove(findTeam);

        return new TeamDto(findTeam);
    }
}
