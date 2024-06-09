package com.jiuth.bigevent.service;

import com.jiuth.bigevent.pojo.User;

public interface UserService {
    User findByUserName(String username);

    void register(String username, String password);

    //更新数据
    void update(User user);

    //更新头像
    void updateAvatar(String avatarUrl);

    void updatePwd(String newPwd);
}
