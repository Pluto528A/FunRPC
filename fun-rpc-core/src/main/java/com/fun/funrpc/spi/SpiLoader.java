package com.fun.funrpc.spi;

import cn.hutool.core.io.resource.Resource;
import cn.hutool.core.io.resource.ResourceUtil;
import com.fun.funrpc.serializer.Serializer;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * SPI 加载器 （键值对映射）
 * @author FUN
 * @version 1.0
 * @date 2024/11/26 20:01
 */
@Slf4j
public class SpiLoader {

    /**
     * 存储已加载的类：接口名-实现类名-实现类
     */
    private static Map<String, Map<String, Class<?>>> loaderMap = new ConcurrentHashMap<>();

    /**
     * 对象实例缓存（避免重复 new 对象），类路径 => 对象实例，单例
     */
    private static Map<String, Object> instanceCache = new ConcurrentHashMap<>();

    /**
     * RPC 系统 SPI 目录
     * META-INF/rpc/system/
     * 存放 RPC 系统的 SPI 实现类
     */
    private static final String RPC_SYSTEM_SPI_DIR = "META-INF/rpc/system/";

    /**
     * RPC 自定义 SPI 目录
     * META-INF/rpc/custom/
     * 存放 RPC 自定义的 SPI 实现类
     */
    private static final String RPC_CUSTOM_SPI_DIR = "META-INF/rpc/custom/";

    /**
     * 扫描 SPI 目录
     */
    private static final String[] SCAN_DIRS = {RPC_SYSTEM_SPI_DIR, RPC_CUSTOM_SPI_DIR};

    /**
     * 动态加载 SPI 实现类
     */
    private static final List<Class<?>> LOAD_CLASS_LIST = Arrays.asList(Serializer.class);

    /**
     * 加载所有 SPI 实现类
     */
    public static void loadAll() {
        log.info("开始加载所有 SPI 实现类");
        for (Class<?> clazz : LOAD_CLASS_LIST) {
            load(clazz);
        }
    }

    /**
     * 获取某个接口的 SPI 实现类
     * @param tClass
     * @param key
     * @return
     * @param <T>
     */
    public static <T> T getInstance(Class<T> tClass, String key) {
        String tClassName = tClass.getName();
        Map<String, Class<?>> keyClassMap = loaderMap.get(tClassName);
        if (keyClassMap == null) {
            throw new RuntimeException(String.format("SpiLoader 未加载 %s 类型 SPI 实现类！", tClassName));
        }

        if (!keyClassMap.containsKey(key)) {
            throw new RuntimeException(String.format("SpiLoader 未找到 %s 类型 SPI 实现类：%s", tClassName, key));
        }

        // 获取到要加载的实现类型
        Class<?> implClass = keyClassMap.get(key);

        // 从实力缓存中加载指定类型的实例
        String implClassName = implClass.getName();
        if (!instanceCache.containsKey(implClassName)) {
            try {
                instanceCache.put(implClassName, implClass.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                String errorMsg = String.format("%s 类实例化失败！", implClassName);
                throw new RuntimeException(errorMsg, e);
            }
        }

        return (T) instanceCache.get(implClassName);
    }

    /**
     * 加载 某个类型的 SPI 实现类
     * @param loadClass 加载的 SPI 接口类
     * @return 键值对映射，键为 SPI 实现类的 key，值为 SPI 实现类
     */
    public static Map<String, Class<?>> load(Class<?> loadClass) {
        log.info("开始加载 SPI 实现类：{}", loadClass.getName());

        // 扫描路径，用户自定义的 SPI 优先级高于系统的 SPI
        Map<String, Class<?>> keyClassMap = new HashMap<>();
        for (String scanDir : SCAN_DIRS) {
            List<URL> resources = ResourceUtil.getResources(scanDir + loadClass.getName());

            // 读取每个资源文件
            for (URL resource : resources) {
                try {
                    InputStreamReader inputStreamReader = new InputStreamReader(resource.openStream());
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String line;
                    while ((line = bufferedReader.readLine())!= null) {
                        String[] strArray = line.split("=");
                        if (strArray.length > 1) {
                            String key = strArray[0];
                            String className = strArray[1];
                            keyClassMap.put(key, Class.forName(className));
                        }
                    }
                } catch (Exception e) {
                    log.error("读取 SPI 实现类配置文件出错：{}", resource.getPath(), e);
                }
            }
        }
        loaderMap.put(loadClass.getName(), keyClassMap);
        return keyClassMap;
    }


}
