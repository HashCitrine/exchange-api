package com.exchange.kafka;

import com.exchange.postgres.entity.Bankstatement;
import com.exchange.postgres.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class OauthProducer {

    private final MemberRepository memberRepository;
    private final KafkaTemplate<Object, Bankstatement> kafkaTemplate;

    public void sendBankStatement(Bankstatement bankstatement){
        log.debug("reqDW bankstatement : {}", bankstatement);
        String reqToken = memberRepository.findMemberToken(bankstatement.getMemberId());
        Long reqTransactionId = bankstatement.getTransactionId();
        // TODO: JSON 묶어서 보내기
        this.kafkaTemplate.send("reqDW", bankstatement);
    }
}
