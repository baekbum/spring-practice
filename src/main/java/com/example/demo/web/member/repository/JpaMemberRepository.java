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
import com.example.demo.web.team.exception.NoSearchTeamException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
//@Primary
@Repository
@RequiredArgsConstructor
public class JpaMemberRepository implements MemberRepository {

    private final EntityManager em;

    @Override
    public Member addMember(InsertMemberParam param) {
        duplicateCheck(param.getId());

        Team findTeam = findTeam(param.getTeamId());

        Member newMember = new Member(param, findTeam);
        em.persist(newMember);

        return newMember;
    }

    private Team findTeam(long teamId) {
        Team findTeam = em.find(Team.class, teamId);

        if (findTeam == null) throw new NoSearchTeamException("해당 팀을 찾을 수 없습니다.");

        return findTeam;
    }

    private void duplicateCheck(String id) {
        String jpql = "SELECT m FROM Member m WHERE m.id = :id";
        TypedQuery<Member> query = em.createQuery(jpql, Member.class);
        query.setParameter("id", id);

        List<Member> resultList = query.getResultList();

        if (!resultList.isEmpty()) throw new MemberDuplicationException("해당 ID는 이미 존재합니다.");
    }

    @Override
    public Member findMember(long memberNo) {
        Member findMember = em.find(Member.class, memberNo);

        if (findMember == null) throw new NoSearchMemberException("해당 멤버를 찾을 수 없습니다.");

        return findMember;
    }

    @Override
    public List<Member> findMembers(MemberCondition condition) {
        StringBuilder jpql = new StringBuilder("SELECT m FROM Member m WHERE 1=1");
        Map<String, Object> paramMaps = new HashMap<>();

        if (condition.getMemberNo() != null) {
            jpql.append(" AND m.memberNo = :memberNo");
            paramMaps.put("memberNo", condition.getMemberNo());
        }

        if (condition.getId() != null) {
            jpql.append(" AND m.id = :id");
            paramMaps.put("id", condition.getId());
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

        return query.getResultList();
    }

    @Override
    public Member updateMember(long memberNo, UpdateMemberParam param) {
        Member findMember = em.find(Member.class, memberNo);

        if (param.getTeamId() != null) {
            param.setTeam(findTeam(param.getTeamId()));
        }

        findMember.updateMember(param);

        return findMember;
    }

    @Override
    public Member deleteMember(long memberNo) {
        Member findMember = em.find(Member.class, memberNo);
        em.remove(findMember);

        return findMember;
    }
}
