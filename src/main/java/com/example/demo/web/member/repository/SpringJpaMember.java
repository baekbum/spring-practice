package com.example.demo.web.member.repository;

import com.example.demo.web.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringJpaMember extends JpaRepository<Member, Long> {

    Optional<Member> findByMemberNo(long memberNo);

    Optional<Member> findByMemberId(String memberId);
}
