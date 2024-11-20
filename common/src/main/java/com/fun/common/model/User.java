package com.fun.common.model;

import java.io.Serializable;

/**
 * 用户
 * @author FUN
 * @version 1.0
 * @date 2024/11/19 21:43
 */
public class User implements Serializable {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
