package com.exchange.api;

import com.exchange.postgres.entity.Member;
import com.exchange.postgres.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private TestService testService;

    @PostMapping("/member")
    @ResponseStatus(HttpStatus.FOUND)
    public List<Member> login(@RequestBody Member member){
        return testService.login(member);
    }
}
