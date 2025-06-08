package com.example.demo.web.member.repository;

import com.example.demo.web.member.dto.InsertMemberParam;
import com.example.demo.web.member.dto.MemberCondition;
import com.example.demo.web.member.dto.MemberDto;
import com.example.demo.web.member.dto.UpdateMemberParam;
import com.example.demo.web.member.exception.MemberDuplicationException;
import com.example.demo.web.member.exception.NoSearchMemberException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
//@Primary
@Repository
@RequiredArgsConstructor
public class MybatisMemberRepository {
/*
public class MybatisMemberRepository implements MemberRepository {

    private final MemberMapper memberMapper;

    @Override
    public MemberDto addMember(InsertMemberParam param) {

        duplicationCheck(param);

        MemberDto memberDto = new MemberDto(param);
        memberMapper.save(memberDto);

        memberDto.setMessage(memberDto.getId() + "을(를) 생성 하였습니다.");

        return memberDto;
    }

    private void duplicationCheck(InsertMemberParam param) {
        // 중복되는 멤버가 있는지 확인하는 절차
        List<MemberDto> findMember = findMembers(new MemberCondition(param.getId()));
        if (!findMember.isEmpty()) throw new MemberDuplicationException("해당 ID는 이미 존재합니다.");
    }

    @Override
    public MemberDto findMember(long memberNo) {
        MemberDto memberDto = memberMapper.findMember(memberNo);

        if (memberDto == null) throw new NoSearchMemberException("해당 멤버를 찾을 수 없습니다.");

        return memberDto;
    }

    @Override
    public List<MemberDto> findMembers(MemberCondition condition) {
        return memberMapper.findMembers(condition);
    }

    @Override
    public MemberDto updateMember(long memberNo, UpdateMemberParam param) {
        MemberDto memberDto = new MemberDto(memberNo, param);

        memberMapper.update(memberDto);

        memberDto.setMessage(memberDto.getName() + "로 수정 하였습니다.");

        return memberDto;
    }

    @Override
    public MemberDto deleteMember(long memberNo) {
        MemberDto memberDto = findMember(memberNo);

        memberMapper.delete(memberNo);

        memberDto.setMessage(memberDto.getId() + "을(를) 삭제 하였습니다.");

        return memberDto;
    }
 */
}
