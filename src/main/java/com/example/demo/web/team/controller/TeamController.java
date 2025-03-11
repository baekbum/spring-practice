package com.example.demo.web.team.controller;

import com.example.demo.web.team.dto.InsertTeamParam;
import com.example.demo.web.team.dto.TeamCondition;
import com.example.demo.web.team.dto.TeamDto;
import com.example.demo.web.team.dto.UpdateTeamParam;
import com.example.demo.web.team.service.TeamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/team")
@RequiredArgsConstructor
public class TeamController {
    
    private final TeamService service;
    
    @PostMapping("/add")
    public ResponseEntity<TeamDto> addTeam(@Validated @RequestBody InsertTeamParam param) {
        return ResponseEntity.ok(service.addTeam(param));
    }

    @GetMapping("/search/{id}")
    public ResponseEntity<TeamDto> findTeam(@PathVariable("id") long id) {
        return ResponseEntity.ok(service.findTeam(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<TeamDto>> findTeams(@RequestBody TeamCondition condition) {
        return ResponseEntity.ok(service.findTeams(condition));
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<TeamDto> updateTeam(@PathVariable("id") long id, @RequestBody UpdateTeamParam param) {
        return ResponseEntity.ok(service.updateTeam(id, param));
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<TeamDto> deleteTeam(@PathVariable("id") long id) {
        return ResponseEntity.ok(service.deleteTeam(id));
    }
}
