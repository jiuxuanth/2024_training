package com.jiuth.springmybatis.service;

import com.jiuth.springmybatis.pojo.User;

public interface UserService {
    public User findUserById(Integer id);
}
