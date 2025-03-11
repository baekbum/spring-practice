package com.example.demo.web.member.repository;

import com.example.demo.web.member.common.MemberRank;
import com.example.demo.web.member.dto.InsertMemberParam;
import com.example.demo.web.member.dto.MemberCondition;
import com.example.demo.web.member.dto.MemberDto;
import com.example.demo.web.member.dto.UpdateMemberParam;
import com.example.demo.web.member.entity.Member;
import com.example.demo.web.member.exception.MemberDuplicationException;
import com.example.demo.web.member.exception.NoSearchMemberException;
import com.example.demo.web.team.entity.Team;
import com.example.demo.web.team.exception.TeamDuplicationException;
import com.example.demo.web.team.repository.SpringJpaTeam;
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
public class SpringJpaMemberRepository implements MemberRepository {

    private final SpringJpaMember memberRepository;
    private final SpringJpaTeam teamRepository;
    private final EntityManager em;


    @Override
    public MemberDto addMember(InsertMemberParam param) {
        duplicateCheck(param);

        Member newMember = new Member(param, findTeam(param.getTeamId()));

        memberRepository.save(newMember);

        return new MemberDto(newMember);
    }

    private Team findTeam(long teamId) {
        return teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamDuplicationException("해당 팀을 찾을 수 없습니다."));
    }

    private void duplicateCheck(InsertMemberParam param) {
        Optional<Member> findMember = memberRepository.findByMemberId(param.getId());

        if (findMember.isPresent()) throw new MemberDuplicationException("해당 ID는 이미 존재합니다.");
    }

    @Override
    public MemberDto findMember(long memberNo) {
        Member findMember = memberRepository.findByMemberNo(memberNo)
                .orElseThrow(() -> new NoSearchMemberException("해당 멤버를 찾을 수 없습니다."));

        return new MemberDto(findMember);
    }

    @Override
    public List<MemberDto> findMembers(MemberCondition condition) {
        StringBuilder jpql = new StringBuilder("SELECT m FROM Member m WHERE 1=1");
        Map<String, Object> paramMaps = new HashMap<>();

        if (condition.getMemberNo() != null) {
            jpql.append(" AND m.memberNo = :memberNo");
            paramMaps.put("memberNo", condition.getMemberNo());
        }

        if (condition.getId() != null) {
            jpql.append(" AND m.memberId = :memberId");
            paramMaps.put("memberId", condition.getId());
        }

        if (condition.getName() != null) {
            jpql.append(" AND m.name = :name");
            paramMaps.put("name", condition.getName());
        }

        if (condition.getBirth() != null) {
            jpql.append(" AND m.birth = :birth");
            paramMaps.put("birth", condition.getBirth());
        }

        if (condition.getRank() != null) {
            jpql.append(" AND m.rank = :rank");
            paramMaps.put("rank", MemberRank.valueOf(condition.getRank()));
        }

        if (condition.getTeamId() != null) {
            Team findTeam = findTeam(condition.getTeamId());
            jpql.append(" AND m.team = :team");
            paramMaps.put("team", findTeam);
        }

        TypedQuery<Member> query = em.createQuery(jpql.toString(), Member.class);

        Set<String> keys = paramMaps.keySet();
        for (String key : keys) {
            query.setParameter(key, paramMaps.get(key));
        }

        List<Member> result = query.getResultList();

        List<MemberDto> findMembers = new ArrayList<>();

        if (!result.isEmpty()) {
            findMembers = result.stream()
                    .map(MemberDto::new)
                    .collect(Collectors.toList());
        }

        return findMembers;
    }

    @Override
    public MemberDto updateMember(long memberNo, UpdateMemberParam param) {
        Member findMember = memberRepository.findByMemberNo(memberNo)
                .orElseThrow(() -> new NoSearchMemberException("해당 멤버를 찾을 수 없습니다."));

        if (param.getTeamId() != null) {
            param.setTeam(findTeam(param.getTeamId()));
        }

        findMember.updateMember(param);

        return new MemberDto(findMember);
    }

    @Override
    public MemberDto deleteMember(long memberNo) {
        Member findMember = memberRepository.findByMemberNo(memberNo)
                .orElseThrow(() -> new NoSearchMemberException("해당 멤버를 찾을 수 없습니다."));

        memberRepository.delete(findMember);

        return new MemberDto(findMember);
    }
}
