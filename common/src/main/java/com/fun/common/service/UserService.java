package com.fun.common.service;

import com.fun.common.model.User;

/**
 * 用户服务接口
 * @author FUN
 * @version 1.0
 * @date 2024/11/19 21:46
 */
public interface UserService {

    /**
     * 获取用户信息
     * @param user 用户对象
     * @return 用户对象
     */
    User getUser(User user);
}
