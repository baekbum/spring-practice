package com.example.demo.web.team.repository;

import com.example.demo.web.team.common.TeamRank;
import com.example.demo.web.team.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringJpaTeam extends JpaRepository<Team, Long> {

    Optional<Team> findByNameAndRankAndUpperTeam(String name, TeamRank rank, Team upperTeam);
}
