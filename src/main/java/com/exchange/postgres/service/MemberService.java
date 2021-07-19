package com.exchange.postgres.service;

import com.exchange.Constants;
import com.exchange.postgres.entity.Bankstatement;
import com.exchange.postgres.entity.Member;
import com.exchange.postgres.entity.Wallet;
import com.exchange.postgres.repository.BankstatementRepository;
import com.exchange.postgres.repository.MemberRepository;
import com.exchange.postgres.repository.WalletRepository;
import com.exchange.utils.JwtAndPassword;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final BankstatementRepository bankstatementRepository;
    private final WalletRepository walletRepository;
    private final JwtAndPassword jwtAndPassword;

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
    public String depositAndWithdraw(Bankstatement bankStatement){
        // 토큰
//        if(member.getToken().equals("")){
//            return "로그인이 안된 사용자입니다.";
//        }

        saveBank(bankStatement);
        saveWallet(bankStatement);

        return "success";
    }

    private void saveBank(Bankstatement bankStatement) {
        Bankstatement newBankStatement = Bankstatement.builder()
                .transactionDate(LocalDateTime.now())
                .memberId(bankStatement.getMemberId())
                .transactionType(bankStatement.getTransactionType())
                .krw(bankStatement.getKrw())
                .build();

        bankstatementRepository.save(newBankStatement);
    }

    private void saveWallet(Bankstatement bankStatement) {
        Wallet existWallet = walletRepository.findByMemberId(bankStatement.getMemberId());
        if (existWallet.getMemberId().equals(bankStatement.getMemberId())){
            log.info("[saveBank]: "+ "duplicated");

            existWallet.setQuantity(checkDepositAndWithdraw(
                    bankStatement.getKrw(), bankStatement.getTransactionType()) + existWallet.getQuantity());

            walletRepository.save(existWallet);
            return;
        }

        Wallet newWallet = Wallet.builder()
                .memberId(bankStatement.getMemberId())
                .currency("MONEY")
                .averagePrice(bankStatement.getKrw())
                .quantity(checkDepositAndWithdraw(bankStatement.getKrw(), bankStatement.getTransactionType()))
                .build();

        walletRepository.save(newWallet);
    }

    private Long checkDepositAndWithdraw(Long krw, Constants.TRANSACTION_TYPE type) {
        if (type == Constants.TRANSACTION_TYPE.DEPOSIT) {
            return krw;
        }
        return -krw;
    }
}
