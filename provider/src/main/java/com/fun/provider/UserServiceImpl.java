package com.fun.provider;

import com.fun.common.model.User;
import com.fun.common.service.UserService;


/**
 * 用户服务实现类
 * @author FUN
 * @version 1.0
 * @date 2024/11/19 21:52
 */
public class UserServiceImpl implements UserService {
    @Override
    public User getUser(User user) {
        System.out.println("用户名：" + user.getName());
        return user;
    }
}
