package com.exchange.api;

import com.exchange.redis.User;
import com.exchange.redis.UserRedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    private UserRedisService userRedisService;

    @PostMapping("/redis/{key}")
    public void put(@PathVariable("key") String key,
                    @RequestBody User user) {

        userRedisService.put(key, user);
    }

    @GetMapping("/redis/{key}")
    public User get(@PathVariable("key") String key) {

        return userRedisService.get(key);
    }

    @DeleteMapping("/redis/{key}")
    public void delete(@PathVariable("key") String key) {

        userRedisService.delete(key);
    }
}
