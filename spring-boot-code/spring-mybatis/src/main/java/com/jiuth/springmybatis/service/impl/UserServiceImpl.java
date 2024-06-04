package com.jiuth.springmybatis.service.impl;

import com.jiuth.springmybatis.mapper.UserMapper;
import com.jiuth.springmybatis.pojo.User;
import com.jiuth.springmybatis.service.UserService;
import org.apache.ibatis.type.Alias;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserMapper userMapper;
    @Override
    public User findUserById(Integer id) {
        return userMapper.findUserById(id);
    }
}
