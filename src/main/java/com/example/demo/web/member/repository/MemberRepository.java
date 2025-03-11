package com.example.demo.web.member.repository;

import com.example.demo.web.member.dto.InsertMemberParam;
import com.example.demo.web.member.dto.MemberCondition;
import com.example.demo.web.member.dto.MemberDto;
import com.example.demo.web.member.dto.UpdateMemberParam;
import com.example.demo.web.member.entity.Member;

import java.util.List;

public interface MemberRepository {

    MemberDto addMember(InsertMemberParam param);

    MemberDto findMember(long memberNo);

    List<MemberDto> findMembers(MemberCondition condition);

    MemberDto updateMember(long memberNo, UpdateMemberParam param);

    MemberDto deleteMember(long memberNo);
}
