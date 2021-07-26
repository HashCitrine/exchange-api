package com.exchange.postgres.service;

import com.exchange.Constants;
import com.exchange.postgres.entity.Bankstatement;
import com.exchange.postgres.entity.Member;
import com.exchange.postgres.entity.Order;
import com.exchange.postgres.entity.Wallet;
import com.exchange.postgres.repository.BankstatementRepository;
import com.exchange.postgres.repository.MemberRepository;
import com.exchange.postgres.repository.OrderRepository;
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

    public String login(Member member) throws Exception {
        Member repoMember = memberRepository.findByMemberId(member.getMemberId());

        if(repoMember == null){
            return "없는 회원입니다.";
        }

        if(!jwtAndPassword.comparePassword(member.getPassword(), repoMember.getPassword())){
            return "비밀번호가 틀렸습니다.";
        }

        repoMember.setToken(jwtAndPassword.makeJwt(member.getMemberId()));
        memberRepository.save(repoMember);

        return "success login";
    }

    public String logout(Member member){
        memberRepository.updateMemberTokenSetNull(member.getMemberId());

        return "success logout";
    }

    public String depositAndWithdraw(Bankstatement bankStatement){
        // 이유 없는(?) 단순 에러 발생시 예외처리
        saveBank(bankStatement);
        saveWallet(bankStatement);

        return "success deposit or withdraw";
    }

    public String authUser(Member member) throws Exception {
        if(!jwtAndPassword.checkJwt(memberRepository
                .findByMemberId(member.getMemberId())
                .getToken())){
            return "failed to user auth";
        }

        return "success user auth";
    }

    private void saveBank(Bankstatement bankStatement) {
        bankStatement.setTransactionDate(LocalDateTime.now());
        bankstatementRepository.save(bankStatement);
    }

    private void saveWallet(Bankstatement bankStatement) {
        walletRepository.updateWallet(checkDepositAndWithdraw(
                bankStatement.getKrw(), bankStatement.getTransactionType()),
                bankStatement.getMemberId());
    }

    private Long checkDepositAndWithdraw(Long krw, Constants.TRANSACTION_TYPE type) {
        if (type == Constants.TRANSACTION_TYPE.WITHDRAW) {
            return -krw;
        }
        return krw;
    }

    public String order(Order order) {
//        order.setOrderId(1L);
//        order.setOrderDate(LocalDateTime.now());
//        order.setStock(order.getQuantity());
//        orderRepository.save(order);
        orderRepository.insertOrder(
                order.getOrderMember(),
                order.getCurrency(),
                order.getOrderType().toString(),
                order.getPrice(),
                order.getQuantity(),
                order.getQuantity());

        return "success order";
    }
}
