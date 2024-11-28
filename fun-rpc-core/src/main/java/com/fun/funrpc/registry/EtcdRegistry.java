package com.fun.funrpc.registry;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ConcurrentHashSet;
import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.Task;
import cn.hutool.json.JSONUtil;
import com.fun.funrpc.config.RegistryConfig;
import com.fun.funrpc.model.ServiceMetaInfo;
import io.etcd.jetcd.*;
import io.etcd.jetcd.options.GetOption;
import io.etcd.jetcd.options.PutOption;
import io.etcd.jetcd.watch.WatchEvent;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * ETCD 注册中心实现
 */
public class EtcdRegistry implements Registry {

    /**
     * 客户端
     */
    private Client client;
    /**
     * KV客户端
     */
    private KV kvClient;

    /**
     * 根路径
     * 为了区分不同的项目，这里的根路径可以根据实际情况进行修改
     */
    private static final String ETCD_ROOT_PATH = "/rpc/";

    /**
     * 本地注册节点的 key 集合
     * 用于服务注册时，判断是否已经注册过
     */
    private final Set<String> localRegisterNodeKeySet = new HashSet<>();

    /**
     * 注册中心服务缓存
     */
    private final RegistryServiceCache registryServiceCache = new RegistryServiceCache();

    /**
     * 正在监听的 key 集合
     */
    private final Set<String> watchingKeySet = new ConcurrentHashSet<>();



    /**
     * 初始化
     * 读取注册中心配置并齿梳化客户端对象
     * @param registryConfig 注册中心配置
     */
    @Override
    public void init(RegistryConfig registryConfig) {
        client = Client.builder()
                .endpoints(registryConfig.getAddress())
                .connectTimeout(Duration.ofMillis(registryConfig.getTimeout()))
                .build();
        kvClient = client.getKVClient();
        // 启动心跳检测
        heartBeat();
    }

    /**
     * 服务注册
     * 创建 key 并设置过期时间，value 为服务注册信息的JSON序列化字符串
     * @param serviceMetaInfo 服务元信息
     * @throws Exception
     */
    @Override
    public void register(ServiceMetaInfo serviceMetaInfo) throws Exception {
        // 创建 lease
        Lease leaseClient = client.getLeaseClient();

        // 创建一个 30 秒的租约
        long leaseId = leaseClient.grant(30).get().getID();

        // 设置要存储的键值对
        String registerKey = ETCD_ROOT_PATH + serviceMetaInfo.getServiceNodeKey();
        ByteSequence key = ByteSequence.from(registerKey, StandardCharsets.UTF_8);
        ByteSequence value = ByteSequence.from(JSONUtil.toJsonStr(serviceMetaInfo), StandardCharsets.UTF_8);

        // 将键值对与租约时间关联起来， 30 秒后自动过期
        PutOption putOption = PutOption.builder().withLeaseId(leaseId).build();
        kvClient.put(key, value, putOption).get();

        // 记录本地注册节点的 key 集合
        localRegisterNodeKeySet.add(registerKey);
    }

    /**
     * 服务注销
     * 删除 key
     * @param serviceMetaInfo 服务元信息
     * @throws Exception
     */
    @Override
    public void unRegister(ServiceMetaInfo serviceMetaInfo) throws Exception {
        String registerKey = ETCD_ROOT_PATH + serviceMetaInfo.getServiceNodeKey();
        kvClient.delete(ByteSequence.from(registerKey, StandardCharsets.UTF_8));
        // 从本地注册节点的 key 集合中移除
        localRegisterNodeKeySet.remove(registerKey);
    }

    /**
     * 服务发现
     * 根据服务名称作为前缀，从ETCD中获取服务下的所有节点，并解析出服务元信息列表
     * @param serviceKey 服务名
     * @return 服务元信息列表
     * @throws Exception
     */
    @Override
    public List<ServiceMetaInfo> serviceDiscovery(String serviceKey) throws Exception {

        List<ServiceMetaInfo> cachaServiceMetaInfoList = registryServiceCache.readCache();
        if (CollUtil.isNotEmpty(cachaServiceMetaInfoList)) {
            return cachaServiceMetaInfoList;
        }

        // 前缀搜索，结尾一定要加 /
        String searchPrefix = ETCD_ROOT_PATH + serviceKey + "/";

        try {
            // 前缀搜索
            GetOption getOption = GetOption.builder().isPrefix(true).build();
            List<KeyValue> keyValues = kvClient.get(
                    ByteSequence.from(searchPrefix, StandardCharsets.UTF_8), getOption)
                    .get()
                    .getKvs();

            // 解析出服务元信息列表
            List<ServiceMetaInfo> serviceMetaInfoList = keyValues.stream()
                    .map(keyValue -> {
                        String key = keyValue.getKey().toString(StandardCharsets.UTF_8);

                        //监听 key 变化
                        watch(key);
                        String value = keyValue.getValue().toString(StandardCharsets.UTF_8);
                        return JSONUtil.toBean(value, ServiceMetaInfo.class);
                    })
                    .collect(Collectors.toList());

            // 缓存服务元信息列表
            registryServiceCache.writeCache(serviceMetaInfoList);

            // 返回服务元信息列表
            return serviceMetaInfoList;

        } catch (Exception e) {
            throw new RuntimeException("服务发现失败", e);
        }
    }

    /**
     * 销毁
     * 释放资源
     */
    @Override
    public void destroy() {
        System.out.println("当前节点下线，清理注册信息");

        // 遍历本地注册节点的 key 集合，删除
        for (String key : localRegisterNodeKeySet) {
            try {
                kvClient.delete(ByteSequence.from(key, StandardCharsets.UTF_8)).get();
            } catch (Exception e) {
                throw new RuntimeException(key + "节点下线失败");
            }
        }

        // 释放资源
        if (client!= null) {
            client.close();
        }
        if (kvClient!= null) {
            kvClient.close();
        }
    }

    /**
     * 心跳检测
     *
     * @throws Exception 心跳异常
     */
    @Override
    public void heartBeat() {
        // 10 秒续签一次
        CronUtil.schedule("*/10 * * * * *", new Task() {
            @Override
            public void execute() {
                // 遍历本节点所有的 key
                for (String key : localRegisterNodeKeySet) {
                    try {
                        List<KeyValue> keyValues = kvClient.get(ByteSequence.from(key, StandardCharsets.UTF_8))
                                .get()
                                .getKvs();
                        // 该节点已过期（需要重启节点才能重新注册）
                        if (CollUtil.isEmpty(keyValues)) {
                            continue;
                        }
                        // 节点未过期，重新注册（相当于续签）
                        KeyValue keyValue = keyValues.get(0);
                        String value = keyValue.getValue().toString(StandardCharsets.UTF_8);
                        ServiceMetaInfo serviceMetaInfo = JSONUtil.toBean(value, ServiceMetaInfo.class);
                        register(serviceMetaInfo);
                    } catch (Exception e) {
                        throw new RuntimeException(key + "续签失败", e);
                    }
                }
            }
        });

        // 支持秒级别定时任务
        CronUtil.setMatchSecond(true);
        CronUtil.start();
    }

    /**
     * 监听服务节点变化
     *
     * @param serviceNodeKey 服务节点key
     */
    @Override
    public void watch(String serviceNodeKey) {
        Watch watchClient = client.getWatchClient();

        // 之前未被监听，开启监听
        boolean newWatch = watchingKeySet.add(serviceNodeKey);

        if (newWatch) {
            watchClient.watch(ByteSequence.from(serviceNodeKey, StandardCharsets.UTF_8), response -> {
                for (WatchEvent event : response.getEvents()) {
                    switch (event.getEventType()) {
                        // key 被删除事触发
                        case DELETE:
                            // 清空注册服务缓存
                            registryServiceCache.clearCache();
                            break;
                        case PUT:
                        default:
                            break;
                    }
                }
            });
        }
    }


}
