package com.example.demo.web.member.repository;

import com.example.demo.web.member.common.MemberRank;
import com.example.demo.web.member.dto.InsertMemberParam;
import com.example.demo.web.member.dto.MemberCondition;
import com.example.demo.web.member.dto.MemberDto;
import com.example.demo.web.member.dto.UpdateMemberParam;
import com.example.demo.web.member.entity.Member;
import com.example.demo.web.member.entity.QMember;
import com.example.demo.web.member.exception.MemberDuplicationException;
import com.example.demo.web.member.exception.NoSearchMemberException;
import com.example.demo.web.team.common.TeamRank;
import com.example.demo.web.team.entity.QTeam;
import com.example.demo.web.team.entity.Team;
import com.example.demo.web.team.exception.TeamDuplicationException;
import com.example.demo.web.team.repository.SpringJpaTeam;
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
public class SpringJpaMemberRepository implements MemberRepository {

    private final JPAQueryFactory queryFactory;
    private final SpringJpaMember memberRepository;
    private final SpringJpaTeam teamRepository;

    @Override
    public Member addMember(InsertMemberParam param) {
        duplicateCheck(param);

        Member newMember = new Member(param, findTeam(param.getTeamId()));

        memberRepository.save(newMember);

        return newMember;
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
    public Member findMember(long memberNo) {
        return memberRepository.findByMemberNo(memberNo)
                .orElseThrow(() -> new NoSearchMemberException("해당 멤버를 찾을 수 없습니다."));
    }

    @Override
    public List<Member> findMembers(MemberCondition condition) {
        QMember member = QMember.member;
        QTeam team = QTeam.team;

        return queryFactory
                .select(member)
                .from(member)
                .leftJoin(member.team, team)
                .where(
                        noEq(condition.getMemberNo(), member),
                        idEq(condition.getId(), member),
                        nameEq(condition.getName(), member),
                        brithEq(condition.getBirth(), member),
                        rankEq(condition.getRank(), member),
                        teamEq(condition.getTeamId(), member)
                ).fetch();
    }

    @Override
    public Member updateMember(long memberNo, UpdateMemberParam param) {
        Member findMember = memberRepository.findByMemberNo(memberNo)
                .orElseThrow(() -> new NoSearchMemberException("해당 멤버를 찾을 수 없습니다."));

        if (param.getTeamId() != null) {
            param.setTeam(findTeam(param.getTeamId()));
        }

        findMember.updateMember(param);

        return findMember;
    }

    @Override
    public Member deleteMember(long memberNo) {
        Member findMember = memberRepository.findByMemberNo(memberNo)
                .orElseThrow(() -> new NoSearchMemberException("해당 멤버를 찾을 수 없습니다."));

        memberRepository.delete(findMember);

        return findMember;
    }

    private BooleanExpression noEq(Long no, QMember member) {
        return no != null ? member.memberNo.eq(no) : null;
    }

    private BooleanExpression idEq(String id, QMember member) {
        return StringUtils.hasText(id) ? member.memberId.eq(id) : null;
    }

    private BooleanExpression nameEq(String name, QMember member) {
        return StringUtils.hasText(name) ? member.name.eq(name) : null;
    }

    private BooleanExpression brithEq(String birth, QMember member) {
        return StringUtils.hasText(birth) ? member.birth.eq(birth) : null;
    }

    private BooleanExpression rankEq(String rank, QMember member) {
        return StringUtils.hasText(rank) ? member.rank.eq(MemberRank.valueOf(rank)) : null;
    }

    private BooleanExpression teamEq(Long teamId, QMember member) {
        if (teamId == null) return null;

        Team findTeam = findTeam(teamId);
        return findTeam != null ? member.team.eq(findTeam) : null;
    }
}
