package com.example.demo.web.member.repository;

import com.example.demo.web.member.dto.MemberCondition;
import com.example.demo.web.member.dto.MemberDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MemberMapper {

    void save(MemberDto dto);

    MemberDto findMember(long memberNo);

    List<MemberDto> findMembers(MemberCondition condition);

    void update(MemberDto dto);

    void delete(long id);
}
