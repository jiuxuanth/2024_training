package com.jiuth.bigevent.service.impl;

import com.jiuth.bigevent.mapper.UserMapper;
import com.jiuth.bigevent.pojo.User;
import com.jiuth.bigevent.service.UserService;
import com.jiuth.bigevent.utils.Md5Util;
import com.jiuth.bigevent.utils.ThreadLocalUtil;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public User findByUserName(String username) {
        return userMapper.findByUserName(username);
    }

    @Override
    public void register(String username, String password) {
        //密码加密
        String md5String = Md5Util.getMD5String(password);

        userMapper.add(username,md5String);
    }

    @Override
    public void update(User user) {
        user.setUpdateTime(LocalDateTime.now());
        userMapper.update(user);
    }

    @Override
    public void updateAvatar(String avatarUrl) {
        Map<String,Object> map=ThreadLocalUtil.get();
        Integer userId=(Integer) map.get("id");
        userMapper.updateAvatar(avatarUrl,userId);
    }

    @Override
    public void updatePwd(String newPwd) {
        Map<String,Object> map=ThreadLocalUtil.get();
        Integer userId=(Integer) map.get("id");
        userMapper.updatePwd(Md5Util.getMD5String(newPwd),userId);
    }
}
