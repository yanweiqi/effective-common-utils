package com.effective.common.netty.cluster.transport.serialization.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.effective.common.netty.cluster.transport.serialization.Serialization;
import com.effective.common.netty.cluster.transport.serialization.Serializer;
import com.effective.common.netty.cluster.transport.serialization.SerializerException;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.alibaba.fastjson.JSON.DEFAULT_GENERATE_FEATURE;

/**
 * Json Serialization
 */
@Slf4j
public class JsonSerialization implements Serialization {

    @Override
    public Byte getContentCode() {
        return JSON_ID;
    }

    @Override
    public List<String> getContentType() {
        return Lists.newArrayList("text/json");
    }

    @Override
    public Serializer getSerializer() {
        return JsonSerializer.INSTANCE;
    }

    /**
     * FASTJSON序列化实现类
     */
    protected static class JsonSerializer implements Serializer {

        protected static final JsonSerializer INSTANCE = new JsonSerializer();

        protected SerializeConfig serializeConfig;

        protected SerializerFeature[] serializerFeatures;

        protected JsonSerializer() {
            serializeConfig = createSerializeConfig();
            serializerFeatures = createSerializerFeatures();
        }

        /**
         * 创建序列化配置
         *
         * @return
         */
        protected SerializeConfig createSerializeConfig() {
            //不采用全局配置，防止用户修改，造成消费者处理错误
            return new SerializeConfig();
        }

        /**
         * 构造序列化特征
         *
         * @return 数组
         */
        protected SerializerFeature[] createSerializerFeatures() {
            return new SerializerFeature[]{SerializerFeature.DisableCircularReferenceDetect};
        }

        @Override
        public <T> void serialize(OutputStream os, T object) throws SerializerException {
            try {
                JSON.writeJSONString(os, StandardCharsets.UTF_8, object, serializeConfig, null, null, DEFAULT_GENERATE_FEATURE, serializerFeatures);
            } catch (IOException e) {
                throw new SerializerException("Error occurred while serializing class " + object.getClass().getName(), e);
            }
        }

        @Override
        public <T> T deserialize(InputStream is, Type type) throws SerializerException {
            try {
                return JSON.parseObject(is, StandardCharsets.UTF_8, type);
            } catch (Exception e) {
                throw new SerializerException("Error occurred while deserializing type " + type, e);
            }
        }
    }
}
