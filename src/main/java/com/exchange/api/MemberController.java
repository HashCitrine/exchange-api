package com.exchange.api;

import com.exchange.Constants;
import com.exchange.postgres.entity.Bankstatement;
import com.exchange.postgres.entity.Member;
import com.exchange.postgres.service.MemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class MemberController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    @Autowired
    private MemberService memberService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public Member register(@RequestBody Member member){
        logger.info(">>> member register");
        return memberService.register(member);
    }

//    @PostMapping("/member")
//    @ResponseStatus(HttpStatus.FOUND)
//    public Object login(@RequestBody Member member) throws Exception {
//        logger.info(">>> member login : " + member.getMemberId());
//        return memberService.login(member);
//    }

    @PostMapping("/member/info")
    @ResponseStatus(HttpStatus.FOUND)
    public Object memberInfo(@RequestBody Member member){
        logger.info(">>> member info : " + member.getMemberId());
        return memberService.memberInfo(member);
    }

    // RequestBody 로 한다.
    @PostMapping("/member/depositAndWithdraw")
    @ResponseStatus(HttpStatus.OK)
    public String depositAndWithdraw(@RequestBody Bankstatement bankStatement){
        return memberService.depositAndWithdraw(bankStatement);
    }
}
