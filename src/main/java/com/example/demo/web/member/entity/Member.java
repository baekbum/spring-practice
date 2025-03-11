package com.example.demo.web.member.entity;

import com.example.demo.web.member.common.MemberRank;
import com.example.demo.web.member.dto.InsertMemberParam;
import com.example.demo.web.member.dto.UpdateMemberParam;
import com.example.demo.web.team.entity.Team;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Member {

    @Id
    @Column(name = "memberNo")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MEMBER_TABLE_SEQUENCE_GENERATOR")
    @SequenceGenerator(name = "MEMBER_TABLE_SEQUENCE_GENERATOR", sequenceName = "MEMBER_TABLE_SEQUENCE", allocationSize = 1)
    private Long memberNo;

    @Column(name = "id")
    private String memberId;
    private String password;
    private String name;
    private String birth;

    @Enumerated(EnumType.STRING)
    private MemberRank rank;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    public Member(InsertMemberParam param, Team team) {
        this.memberId = param.getId();
        this.password = param.getPassword();
        this.name = param.getName();
        this.birth = param.getBrith();
        this.rank = MemberRank.valueOf(param.getRank());
        this.team = team;
    }

    public void updateMember(UpdateMemberParam param) {
        this.password = (param.getPassword() != null) ? param.getPassword() : this.password;
        this.name = (param.getName() != null) ? param.getName() : this.name;
        this.birth = (param.getBrith() != null) ? param.getBrith() : this.birth;
        this.rank = (param.getRank() != null) ? MemberRank.valueOf(param.getRank()) : this.rank;
        this.team = (param.getTeam() != null) ? param.getTeam() : this.team;
    }
}
