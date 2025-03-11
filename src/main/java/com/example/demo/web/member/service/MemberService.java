package com.example.demo.web.member.service;

import com.example.demo.web.member.dto.InsertMemberParam;
import com.example.demo.web.member.dto.MemberCondition;
import com.example.demo.web.member.dto.MemberDto;
import com.example.demo.web.member.dto.UpdateMemberParam;
import com.example.demo.web.member.entity.Member;
import com.example.demo.web.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository repository;

    public MemberDto addMember(InsertMemberParam param) {
        return repository.addMember(param);
    }

    @Transactional(readOnly = true)
    public MemberDto findMember(long memberNo) {
        return repository.findMember(memberNo);
    }

    @Transactional(readOnly = true)
    public List<MemberDto> findMembers(MemberCondition condition) {
        return repository.findMembers(condition);
    }

    public MemberDto updateMember(long memberNo, UpdateMemberParam param) {
        return repository.updateMember(memberNo, param);
    }

    public MemberDto deleteMember(long memberNo) {
        return repository.deleteMember(memberNo);
    }

}
