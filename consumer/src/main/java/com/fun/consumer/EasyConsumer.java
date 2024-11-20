package com.fun.consumer;

import com.fun.common.model.User;
import com.fun.common.service.UserService;
import com.fun.funrpc.proxy.ServiceProxyFactory;

/**
 * 简单 RPC 消费者示例
 * @author FUN
 * @version 1.0
 * @date 2024/11/19 21:59
 */
public class EasyConsumer {
    public static void main(String[] args) {
        // 这里需要获取 UserService 的具体实现类，并实例化
        //UserService userService = new UserServiceProxy();
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
        User user = new User();
        user.setName("fun");

        User newUser = userService.getUser(user);
        if (newUser!= null) {
            System.out.println(newUser.getName());
        } else {
            System.out.println("User not found.");
        }
    }
}
