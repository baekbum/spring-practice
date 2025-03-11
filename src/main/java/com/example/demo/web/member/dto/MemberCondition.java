package com.example.demo.web.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemberCondition {

    private Long memberNo;
    private String id;
    private String name;
    private String birth;
    private String rank;
    private Long teamId;
    private String teamName;

    public MemberCondition(String id) {
        this.id = id;
    }
}
