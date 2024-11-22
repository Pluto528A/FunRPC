package com.fun.funrpc.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.dialect.Props;

/**
 * 配置工具类
 * @author FUN
 * @version 1.0
 * @date 2024/11/22 20:48
 */
public class ConfigUtils {

    /**
     * 加载配置
     * @param tClass 配置类
     * @param prefix 配置前缀
     * @param <T> 配置类类型
     * @return 配置类实例
     */
    public static <T> T loadConfig(Class<T> tClass, String prefix) {
        return loadConfig(tClass, prefix, "");
    }

    /**
     * 加载配置
     * @param tClass 配置类
     * @param prefix 配置前缀
     * @param environment 环境
     * @param <T> 配置类类型
     * @return 配置类实例
     */
    public static  <T> T loadConfig(Class<T> tClass, String prefix, String environment) {
        StringBuilder configFileBuilder = new StringBuilder("application");
        if (StrUtil.isNotBlank(environment)) {
            configFileBuilder.append("-").append(environment);
        }
        configFileBuilder.append(".properties");
        Props props = new Props(configFileBuilder.toString());

        return props.toBean(tClass, prefix);
    }
}
