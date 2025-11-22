package com.effective.common.netty.cluster.transport.serialization.protostuff;

import com.effective.common.netty.cluster.transport.serialization.Serialization;
import com.effective.common.netty.cluster.transport.serialization.Serializer;
import com.effective.common.netty.cluster.transport.serialization.SerializerException;
import com.google.common.collect.Lists;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.DefaultIdStrategy;
import io.protostuff.runtime.IdStrategy;
import io.protostuff.runtime.RuntimeSchema;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Protostuff Serialization
 */
@Slf4j
public class ProtostuffSerialization implements Serialization {

    @Override
    public Byte getContentCode() {
        return (byte) PROTOSTUFF_ID;
    }

    /**
     * 获取内容格式
     *
     * @return
     */
    @Override
    public List<String> getContentType() {
        return Lists.newArrayList("application/x-protostuff");
    }

    /**
     * 构建序列化器
     *
     * @return
     */
    @Override
    public Serializer getSerializer() {
        return ProtostuffSerializer.INSTANCE;
    }

    /**
     * Protostuff序列化实现类
     */
    protected static class ProtostuffSerializer implements Serializer {

        protected static final ProtostuffSerializer INSTANCE = new ProtostuffSerializer();

        protected static final DefaultIdStrategy STRATEGY = new DefaultIdStrategy(IdStrategy.DEFAULT_FLAGS | IdStrategy.ALLOW_NULL_ARRAY_ELEMENT);

        protected ThreadLocal<LinkedBuffer> local = ThreadLocal.withInitial(() -> LinkedBuffer.allocate(1024));

        protected ProtostuffSerializer() {
        }

        /**
         * 序列化
         *
         * @param os     输出流
         * @param object 对象
         * @throws SerializerException
         */
        @Override
        public <T> void serialize(OutputStream os, T object) throws SerializerException {
            LinkedBuffer linkedBuffer = local.get();
            try {
                Schema<T> schema = RuntimeSchema.getSchema((Class<T>) object.getClass(), STRATEGY);
                ProtostuffIOUtil.writeTo(os, object, schema, linkedBuffer);
            } catch (IOException e) {
                log.error("Serializing object by protostuff error! message={}", e.getMessage(), e);
                throw new SerializerException("Error occurred while serializing class " + object.getClass().getName(), e);
            } finally {
                linkedBuffer.clear();
            }
        }

        /**
         * 反序列化
         *
         * @param is
         * @param type
         * @return
         * @throws SerializerException
         */
        @Override
        public <T> T deserialize(InputStream is, Type type) throws SerializerException {
            if (!(type instanceof Class)) {
                throw new SerializerException("Argument type must be a Class " + type);
            }
            LinkedBuffer linkedBuffer = local.get();
            try {
                Class<T> clazz = (Class<T>) type;
                Schema<T> schema = RuntimeSchema.getSchema(clazz, STRATEGY);
                T obj = schema.newMessage();
                ProtostuffIOUtil.mergeFrom(is, obj, schema, linkedBuffer);
                return obj;
            } catch (IOException e) {
                log.error("Deserializing object by protostuff error! message={}", e.getMessage(), e);
                throw new SerializerException("Error occurred while deserializing class " + type, e);
            } finally {
                linkedBuffer.clear();
            }
        }
    }

}
