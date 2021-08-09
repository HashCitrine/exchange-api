package com.exchange.postgres.service;

import com.exchange.postgres.entity.Member;
import com.exchange.postgres.entity.Wallet;
import com.exchange.postgres.repository.MemberRepository;
import com.exchange.postgres.repository.OrderRepository;
import com.exchange.postgres.repository.WalletRepository;
import com.exchange.utils.JwtAndPassword;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Log4j2
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final WalletRepository walletRepository;
    private final OrderRepository orderRepository;
    private final JwtAndPassword jwtAndPassword;

    public Member memberInfo(Member member) {
        return memberRepository.findByMemberId(member.getMemberId());
    }

    public String register(Member member) {
        if (member.getMemberId().equals("") || member.getPassword().equals("")){
            return "아이디와 비밀번호는 반드시 입력해주세요.";
        }

        if (memberRepository.findByMemberId(member.getMemberId()) != null){
            return "이미 아이디가 존재합니다.";
        }

        Member newMember = Member.builder()
                .memberId(member.getMemberId())
                .password(jwtAndPassword.hashPassword(member.getPassword()))
                .role(member.getRole())
                .useYn(member.getUseYn())
                .regDate(LocalDateTime.now())
                .uptDate(LocalDateTime.now())
                .build();

        memberRepository.save(newMember);

        Wallet newWallet = Wallet.builder()
                .memberId(member.getMemberId())
                .currency("MONEY")
                .averagePrice(0L)
                .quantity(0L)
                .build();

        walletRepository.save(newWallet);

        return "success register";
    }
}