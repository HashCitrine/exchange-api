package com.exchange.api;

import com.exchange.Constants;
import com.exchange.postgres.entity.Bankstatement;
import com.exchange.postgres.entity.Member;
import com.exchange.postgres.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

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

    @PostMapping("/member/auth")
    @ResponseStatus(HttpStatus.OK)
    public String authUser(@RequestBody Member member) throws Exception {
        return memberService.authUser(member);
    }

    @PostMapping("/member/info")
    @ResponseStatus(HttpStatus.FOUND)
    public Object memberInfo(@RequestBody Member member){
        log.info(">>> member info : " + member.getMemberId());
        return memberService.memberInfo(member);
    }

    @PostMapping("/member/depositAndWithdraw")
    @ResponseStatus(HttpStatus.OK)
    public String depositAndWithdraw(@RequestBody Bankstatement bankStatement){
        return memberService.depositAndWithdraw(bankStatement);
    }
}
