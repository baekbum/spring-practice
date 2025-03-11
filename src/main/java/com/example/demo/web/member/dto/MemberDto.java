package com.example.demo.web.member.dto;

import com.example.demo.web.common.dto.ResponseDto;
import com.example.demo.web.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class MemberDto extends ResponseDto {

    private long memberNo;
    private String id;
    private String password;
    private String name;
    private String birth;
    private String rank;
    private Long teamId;
    private String teamName;

    public MemberDto(long memberNo, String id, String name, String birth, String rank, Long teamId, String teamName) {
        this.memberNo = memberNo;
        this.id = id;
        this.name = name;
        this.birth = birth;
        this.rank = rank;
        this.teamId = teamId;
        this.teamName = teamName;
    }

    public MemberDto(InsertMemberParam param) {
        this.id = param.getId();
        this.password = param.getPassword();
        this.name = param.getName();
        this.birth = param.getBrith();
        this.rank = param.getRank();
        this.teamId = param.getTeamId();
    }

    public MemberDto(long memberNo, UpdateMemberParam param) {
        this.memberNo = memberNo;
        this.password = param.getPassword();
        this.name = param.getName();
        this.birth = param.getBrith();
        this.rank = param.getRank();
        this.teamId = param.getTeamId();
    }

    public MemberDto(Member member) {
        this.memberNo = member.getMemberNo();
        this.id = member.getMemberId();
        this.password = member.getPassword();
        this.name = member.getName();
        this.birth = member.getBirth();
        this.rank = member.getRank().toString();
        this.teamId = member.getTeam().getId();
        this.teamName = member.getTeam().getName();
    }
}
