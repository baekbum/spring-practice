package com.example.demo.web.member.repository;

import com.example.demo.web.member.dto.InsertMemberParam;
import com.example.demo.web.member.dto.MemberCondition;
import com.example.demo.web.member.dto.MemberDto;
import com.example.demo.web.member.dto.UpdateMemberParam;
import com.example.demo.web.member.entity.Member;

import java.util.List;

public interface MemberRepository {

    Member addMember(InsertMemberParam param);

    Member findMember(long memberNo);

    List<Member> findMembers(MemberCondition condition);

    Member updateMember(long memberNo, UpdateMemberParam param);

    Member deleteMember(long memberNo);
}
