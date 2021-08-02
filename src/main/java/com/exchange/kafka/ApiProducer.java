package com.exchange.kafka;

import com.exchange.Constants;
import com.exchange.postgres.entity.BankStatement;
import com.exchange.postgres.repository.MemberRepository;
import com.exchange.postgres.service.MemberService;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Log4j2
public class ApiProducer {

    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public String sendBankStatement(BankStatement bankStatement, String token){
        memberService.saveBankStatement(bankStatement);
        processKafka(token, Long.toString(bankStatement.getTransactionId()));

        return "success request oauth";
    }

    private void processKafka(String token, String reqTransactionId) {
        Gson reqGsonObj = new Gson();

        Map<String, String> inputMap = new HashMap<String, String>();
        inputMap.put("token", token);
        inputMap.put("transactionId", reqTransactionId);

        String jsonStr = reqGsonObj.toJson(inputMap);
        log.info("[ApiProducer] jsonStr {}", jsonStr);

        this.kafkaTemplate.send(Constants.TOPIC.reqDw.toString(), jsonStr);
    }
}
