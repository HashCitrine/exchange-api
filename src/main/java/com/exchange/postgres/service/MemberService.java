package com.exchange.postgres.service;

import com.exchange.Constants;
import com.exchange.postgres.entity.BankStatement;
import com.exchange.postgres.entity.Member;
import com.exchange.postgres.entity.Order;
import com.exchange.postgres.entity.Wallet;
import com.exchange.postgres.repository.BankStatementRepository;
import com.exchange.postgres.repository.MemberRepository;
import com.exchange.postgres.repository.OrderRepository;
import com.exchange.postgres.repository.WalletRepository;
import com.exchange.utils.JwtAndPassword;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final BankStatementRepository bankStatementRepository;
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

    public void saveBankStatement(BankStatement bankStatement){
        try {
            updateBankStatementStatus(bankStatement, Constants.STATUS.REQS);
        }catch(Exception e){
            log.info("error save bank statement");
            updateBankStatementStatus(bankStatement, Constants.STATUS.REQF);
        }
    }

    private void updateBankStatementStatus(BankStatement bankStatement, Constants.STATUS status) {
        bankStatement.setTransactionDate(LocalDateTime.now());
        bankStatement.setStatus(status);
        bankStatementRepository.save(bankStatement);
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
