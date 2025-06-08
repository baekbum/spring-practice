package com.example.demo.web.team.repository;

import com.example.demo.web.member.exception.MemberDuplicationException;
import com.example.demo.web.team.entity.Team;
import com.example.demo.web.team.exception.TeamDuplicationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Repository
public class MemoryTeamRepository  {
    /*
    private List<Team> storage = new ArrayList<>();
    private AtomicLong id = new AtomicLong(0);

    @Override
    public void addTeam(Team team) {

        if (duplicationCheck(team)) throw new TeamDuplicationException("해당 팀은 이미 존재합니다.");

        team.setId(id.incrementAndGet());

        storage.add(team);

        log.info("추가 된 팀 : " + team.toString());
    }

    @Override
    public Team findTeam(long id) {
        return storage.stream().filter(t -> t.getId() == id).findAny()
                .orElseThrow(() -> new IllegalArgumentException("해당 팀은 존재하지 않습니다."));
    }

    @Override
    public List<Team> findTeams() {
        return storage;
    }

    @Override
    public Team updateTeam(long id, Team team) {
        Team findTeam = findTeam(id);
        log.info("[기존 팀 데이터] : " + findTeam.toString());
        findTeam = team;
        log.info("[바뀐 팀 데이터] : " + findTeam.toString());

        return findTeam;
    }

    @Override
    public Team deleteTeam(long id) {
        Team findTeam = findTeam(id);
        storage.remove(findTeam);

        log.info("[삭제된 팀 데이터] : " + findTeam.toString());

        return findTeam;
    }

    private boolean duplicationCheck(Team team) {
        return storage.stream().anyMatch(element -> element.equals(team));
    }

    @Override
    public void deleteAllTeam() {
        storage = new ArrayList<>();
        id.set(0);
        log.info("[리스트 초기화] : " + storage.size());
    }
     */
}
