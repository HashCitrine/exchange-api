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
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@Service
@Slf4j
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final BankstatementRepository bankstatementRepository;
    private final WalletRepository walletRepository;
//    private final JwtAndPassword jwtAndPassword;

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
        log.info("member info: " + member);

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

    @ExceptionHandler(value = Exception.class)
    public String depositAndWithdraw(String memberId, Long krw,
                                     Constants.TRANSACTION_TYPE type){
        // 토큰
//        if(member.getToken().equals("")){
//            return "로그인이 안된 사용자입니다.";
//        }

        saveBank(memberId, krw, type);
        saveWallet(memberId, krw, type);

        return "success";
    }

    private void saveBank(String memberId, Long krw, Constants.TRANSACTION_TYPE type) {
        // 빌더 패턴 => set 안써도됨. (생성자임)

        Bankstatement newBankStatement = new Bankstatement();
        newBankStatement.setTransactionId(1L);
        newBankStatement.setTransactionDate(LocalDateTime.now());
        newBankStatement.setMemberId(memberId);
        newBankStatement.setTransactionType(type);
        newBankStatement.setKrw(krw);

        bankstatementRepository.save(newBankStatement);
    }

    private void saveWallet(String memberId, Long krw, Constants.TRANSACTION_TYPE type) {
        Wallet existWallet = walletRepository.findByMemberId(memberId);
        if (existWallet.getMemberId().equals(memberId)){
            log.info("[saveBank]: "+ "duplicated");

            existWallet.setQuantity(checkDepositAndWithdraw(krw, type) + existWallet.getQuantity());
            walletRepository.save(existWallet);
            return;
        }

        Wallet newWallet = new Wallet();
        newWallet.setMemberId(memberId);
        newWallet.setCurrency("MONEY");
        newWallet.setAveragePrice(0L);
        newWallet.setQuantity(checkDepositAndWithdraw(krw, type));

        walletRepository.save(newWallet);
    }

    private Long checkDepositAndWithdraw(Long krw, Constants.TRANSACTION_TYPE type) {
        if (type == Constants.TRANSACTION_TYPE.DEPOSIT) {
            return krw;
        }
        return -krw;
    }
}
