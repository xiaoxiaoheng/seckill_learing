package com.miaomiao.controller;

import com.miaomiao.domain.User;
import com.miaomiao.redis.UserKey;
import com.miaomiao.result.Result;
import com.miaomiao.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by cjq on 2018-03-07 13:30
 */
@Controller
@RequestMapping("/demo")
public class SampleController {
    @Autowired
    RedisService redisService;

    @RequestMapping("/thymeleaf")
    public String thymeleaf(Model model) {
        model.addAttribute("name" , "cjq");
        return "hello";
    }

    @RequestMapping("/redis/get")
    @ResponseBody
    public Result<User> redisGet() {
        User v1 = redisService.get(UserKey.getById ,String.valueOf(1) , User.class);
        return Result.success(v1);
    }

    @RequestMapping("/redis/set")
    @ResponseBody
    public Result<Boolean> redisSet() {
        User user = new User();
        user.setId(1);
        user.setName("jack");
        boolean v1 = redisService.set(UserKey.getById ,String.valueOf(1) , user);
        return Result.success(v1);
    }
}
