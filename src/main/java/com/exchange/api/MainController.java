package com.exchange.api;

import com.exchange.Constants;
import com.exchange.kafka.ApiProducer;
import com.exchange.postgres.entity.BankStatement;
import com.exchange.postgres.entity.Member;
import com.exchange.postgres.entity.Order;
import com.exchange.postgres.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
public class MainController {

    private final MemberService memberService;
    private final ApiProducer producer;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public String register(@RequestBody Member member){
        return memberService.register(member);
    }

    @PostMapping("/member/info")
    @ResponseStatus(HttpStatus.FOUND)
    public Object memberInfo(@RequestBody Member member){
        log.info(">>> member info : " + member.getMemberId());
        return memberService.memberInfo(member);
    }

    @PostMapping("/charge")
    @ResponseStatus(HttpStatus.OK)
    public String reqOauth(@RequestBody BankStatement bankStatement,
                           @RequestParam String token){
        return producer.normalProcess(bankStatement, Constants.TOPIC.reqDw, token);
    }

    @PostMapping("/order")
    @ResponseStatus(HttpStatus.OK)
    public String order(@RequestBody Order order, @RequestParam String token){
        return producer.normalProcess(order, Constants.TOPIC.reqOrder, token);
    }
}
