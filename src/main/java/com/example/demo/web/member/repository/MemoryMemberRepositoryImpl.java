package com.example.demo.web.member.repository;

import com.example.demo.web.member.entity.Member;
import com.example.demo.web.member.exception.MemberDuplicationException;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class MemoryMemberRepositoryImpl {

    /*
    private List<Member> storage = new ArrayList<>();
    private AtomicLong id = new AtomicLong(0);

    @Override
    public void addMember(Member member) {

        if (duplicationCheck(member)) throw new MemberDuplicationException("해당 사용자는 이미 존재합니다.");

        member.setId(id.incrementAndGet());
        storage.add(member);
    }

    @Override
    public Member findMember(long id) {
        return storage.stream().filter(m -> m.getId() == id).findAny()
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자는 존재하지 않습니다."));
    }

    @Override
    public List<Member> findMembers() {
        return storage;
    }

    @Override
    public void updateMember(long id, Member member) {
        Member findMember = findMember(id);
        findMember = member;
    }

    @Override
    public void deleteMember(long id) {
        storage.remove(findMember(id));
    }

    private boolean duplicationCheck(Member member) {
        return storage.stream().anyMatch(element -> element.equals(member));
    }
     */
}
