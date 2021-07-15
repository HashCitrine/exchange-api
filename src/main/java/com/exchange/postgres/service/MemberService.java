package com.exchange.postgres.service;

import com.exchange.Constants;
import com.exchange.postgres.entity.Bankstatement;
import com.exchange.postgres.entity.Member;
import com.exchange.postgres.entity.Wallet;
import com.exchange.postgres.repository.BankstatementRepository;
import com.exchange.postgres.repository.MemberRepository;
import com.exchange.postgres.repository.WalletRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Slf4j
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final BankstatementRepository bankstatementRepository;
    private final WalletRepository walletRepository;
//    private final JwtAndPassword jwtAndPassword;

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    public MemberService(MemberRepository memberRepository, BankstatementRepository bankstatementRepository, WalletRepository walletRepository){
        this.memberRepository = memberRepository;
//        this.jwtAndPassword = jwtAndPassword;
        this.bankstatementRepository = bankstatementRepository;
        this.walletRepository = walletRepository;
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

//    public Member login(Member member) throws Exception {
//        Member repoMember = memberRepository.findByMemberId(member.getMemberId());
//
//        boolean checkPassword = jwtAndPassword.comparePassword(member.getPassword(), repoMember.getPassword());
//
//        if(!checkPassword){
//            throw new NotFoundException("wrong password...");
//        }
//
//        String token = jwtAndPassword.makeJwt(member.getMemberId());
//        repoMember.setToken(token);
//
//        return memberRepository.save(repoMember);
//    }
//
//    public String logout(Member member){
//        member.setToken("");
//        memberRepository.save(member);
//
//        return "success";
//    }

    public String depositRequest(String memberId, Long krw,
                                 Constants.TRANSACTION_TYPE type){
        // 토큰
//        if(member.getToken().equals("")){
//            return "로그인이 안된 사용자입니다.";
//        }

        logger.info("[service]: " + memberId);
        logger.info("[service]: " + krw);
        logger.info("[service]: " + type);

        Bankstatement newBankStatement = new Bankstatement();
        newBankStatement.setTransactionId(1L);
        newBankStatement.setTransactionDate(LocalDateTime.now());
        newBankStatement.setMemberId(memberId);
        newBankStatement.setTransactionType(type);
        newBankStatement.setBank("국민은행");
        newBankStatement.setKrw(krw);

        bankstatementRepository.save(newBankStatement);

        Wallet newWallet = new Wallet();
        newWallet.setMemberId(memberId);
        newWallet.setCurrency("MONEY");
        newWallet.setAveragePrice(0L);

        Long tmp = 0L;
        if (type == Constants.TRANSACTION_TYPE.DEPOSIT) {
            tmp += krw;
        } else {
            tmp -= krw;
        }
        newWallet.setQuantity(tmp);

        walletRepository.save(newWallet);

        return "success";
    }
}
