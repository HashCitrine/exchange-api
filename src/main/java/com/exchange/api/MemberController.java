package com.exchange.api;

import com.exchange.kafka.OauthProducer;
import com.exchange.postgres.entity.Bankstatement;
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
public class MemberController {

    private final MemberService memberService;
    private final OauthProducer producer;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public String register(@RequestBody Member member){
        return memberService.register(member);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.FOUND)
    public String login(@RequestBody Member member) throws Exception {
        return memberService.login(member);
    }

    @PostMapping("/member/logout")
    @ResponseStatus(HttpStatus.OK)
    public String logout(@RequestBody Member member){
        return memberService.logout(member);
    }

    @PostMapping("/member/info")
    @ResponseStatus(HttpStatus.FOUND)
    public Object memberInfo(@RequestBody Member member){
        log.info(">>> member info : " + member.getMemberId());
        return memberService.memberInfo(member);
    }

    @PostMapping("/member/reqOauth")
    @ResponseStatus(HttpStatus.OK)
    public String reqOauth(@RequestBody Bankstatement bankstatement){
        this.producer.sendBankStatement(bankstatement);
        return "success request oauth";
    }

    @PostMapping("/member/order")
    @ResponseStatus(HttpStatus.OK)
    public String order(@RequestBody Order order){
        return memberService.order(order);
    }
}
