package com.example.demo.web.team.repository;

import com.example.demo.web.team.common.TeamRank;
import com.example.demo.web.team.dto.*;
import com.example.demo.web.team.entity.QTeam;
import com.example.demo.web.team.entity.Team;
import com.example.demo.web.team.exception.NoSearchTeamException;
import com.example.demo.web.team.exception.TeamDuplicationException;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Primary
@Repository
@RequiredArgsConstructor
public class SpringJpaTeamRepository implements TeamRepository {

    private final JPAQueryFactory queryFactory;
    private final SpringJpaTeam repository;
    private final EntityManager em;

    @Override
    public Team addTeam(InsertTeamParam param) {
        Team upperTeam = findUpperTeam(param.getUpperTeamId());

        duplicateCheck(param, upperTeam);

        Team newTeam = new Team(param, upperTeam);
        repository.save(newTeam);

        return newTeam;
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
    public Team findTeam(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NoSearchTeamException("해당 팀을 찾을 수 없습니다."));
    }

    @Override
    public List<Team> findTeams(TeamCondition condition) {
        QTeam team = QTeam.team;

        return queryFactory
                .select(team)
                .from(team)
                .where(
                        idEq(condition.getId(), team),
                        nameContains(condition.getName(), team),
                        rankEq(condition.getRank(), team),
                        upperTeamEq(condition.getUpperTeamId(), team)
                )
                .fetch();
    }

    @Override
    public Team updateTeam(long id, UpdateTeamParam param) {
        Team findTeam = repository.findById(id).orElseThrow(() -> new NoSearchTeamException("해당 팀을 찾을 수 없습니다."));

        if (param.getUpperTeamId() != null) {
            param.setUpperTeam(repository.findById(param.getUpperTeamId()).get());
        }

        findTeam.updateTeam(param);

        return findTeam;
    }

    @Override
    public Team deleteTeam(long id) {
        Team findTeam = repository.findById(id).orElseThrow(() -> new NoSearchTeamException("해당 팀을 찾을 수 없습니다."));

        repository.delete(findTeam);

        return findTeam;
    }

    private BooleanExpression idEq(Long id, QTeam team) {
        return id != null ? team.id.eq(id) : null;
    }

    private BooleanExpression nameContains(String name, QTeam team) {
        return StringUtils.hasText(name) ? team.name.contains(name) : null;
    }

    private BooleanExpression rankEq(String name, QTeam team) {
        return StringUtils.hasText(name) ? team.rank.eq(TeamRank.valueOf(name)) : null;
    }

    private BooleanExpression upperTeamEq(Long id, QTeam team) {
        return id != null ? team.upperTeam.eq(findUpperTeam(id)) : null;
    }
}
