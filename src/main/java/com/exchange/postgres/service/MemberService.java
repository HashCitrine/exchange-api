package com.exchange.postgres.service;

import com.exchange.Constants;
import com.exchange.postgres.entity.Member;
import com.exchange.postgres.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class MemberService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    @Autowired
    private MemberRepository memberRepository;

    public List<Member> findAll(){
        return memberRepository.findAll();
    }

//    public List<Member> login(Member member){
//        return memberRepository.findByMemberIdAndPassword(member.getMemberId(), member.getPassword());
//    }

    public Object login(Member member){
        return memberRepository.findMember(member.getMemberId());
    }

    public Member memberInfo(Member member) {
        return memberRepository.findByMemberId(member.getMemberId());
    }

    public Member register(Member member) {
        logger.info("member info: " + member);

        member.setRegDate(LocalDateTime.now());
        member.setUptDate(LocalDateTime.now());

        return memberRepository.save(member);
    }
}
