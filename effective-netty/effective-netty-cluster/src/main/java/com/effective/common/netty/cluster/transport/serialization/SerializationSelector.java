package com.effective.common.netty.cluster.transport.serialization;


import com.effective.common.netty.cluster.transport.serialization.json.JsonSerialization;
import com.effective.common.netty.cluster.transport.serialization.protostuff.ProtostuffSerialization;
import lombok.extern.slf4j.Slf4j;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 序列化选择器
 */
@Slf4j
public class SerializationSelector {

    private static Map<String, Serialization> serializationMap = new ConcurrentHashMap<>();

    private static Map<Byte, Serialization> serializationCodeMap = new ConcurrentHashMap<>();

    /**
     * 增加序列化处理器
     *
     * @param serialization 序列化处理器
     */
    public static void addHandler(Serialization serialization) {
        serialization.getContentType().forEach(contentType -> {
            if (!serializationMap.containsKey(contentType)) {
                serializationMap.computeIfAbsent(contentType, s -> serialization);
                serializationCodeMap.computeIfAbsent(serialization.getContentCode(), b -> serialization);
            } else {
                log.error("添加序列 contentType:{} class:{}", contentType, serialization.getClass().getCanonicalName());
            }
        });
    }

    /**
     * 获取序列化实现类
     *
     * @param contentType 类型
     * @return 序列化实现类
     */
    public static Serialization select(String contentType) {
        if(serializationMap.isEmpty()){
            addHandler(new JsonSerialization());
            addHandler(new ProtostuffSerialization());
        }
        return serializationMap.get(contentType);
    }

    /**
     * 获取序列化实现类
     *
     * @param contentType 类型
     * @return 序列化实现类
     */
    public static Serialization select(Byte contentType) {
        if(serializationCodeMap.isEmpty()){
            addHandler(new JsonSerialization());
            addHandler(new ProtostuffSerialization());
        }
        return serializationCodeMap.get(contentType);
    }

}
