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
        return "success register";
    }

    public String login(Member member) throws Exception {
        Member repoMember = memberRepository.findByMemberId(member.getMemberId());

        if(repoMember == null){
            return "없는 회원입니다.";
        }

        boolean checkPassword = jwtAndPassword.comparePassword(member.getPassword(), repoMember.getPassword());

        if(!checkPassword){
            return "비밀번호가 틀렸습니다.";
        }

        String token = jwtAndPassword.makeJwt(member.getMemberId());
        repoMember.setToken(token);

        memberRepository.save(repoMember);
        return "success login";
    }

    public String logout(Member member){
        // 멤버를 가지고와서 set해야 그거(토큰)만 저장된다.
        Member repoMember = memberRepository.findByMemberId(member.getMemberId());

        repoMember.setToken("");
        memberRepository.save(repoMember);

        return "success logout";
    }

    @ExceptionHandler(value = Exception.class)
    public String depositAndWithdraw(Bankstatement bankStatement){
        saveBank(bankStatement);
        saveWallet(bankStatement);

        return "success deposit or withdraw";
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

    public String authUser(Member member) throws Exception {
        if(jwtAndPassword
                .checkJwt(memberRepository
                .findByMemberId(member.getMemberId())
                .getToken())){
            return "success user auth";
        }

        return "fail user auth";
    }
}
