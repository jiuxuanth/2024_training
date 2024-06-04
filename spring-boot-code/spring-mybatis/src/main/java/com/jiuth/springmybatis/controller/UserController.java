package com.jiuth.springmybatis.controller;

import com.jiuth.springmybatis.pojo.User;
import com.jiuth.springmybatis.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;
    @RequestMapping("/findById")
    public User findById(Integer id) {
        return userService.findUserById(id);
    }
}
