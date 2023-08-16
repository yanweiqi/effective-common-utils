package com.effective.common.netty.cluster.transport.serialization;


import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;

/**
 * 对象序列化
 */
public interface Serializer {

    /**
     * 序列化
     *
     * @param out 输出流
     * @param t  序列化对象
     * @throws SerializerException 序列化异常
     */
    <T> void serialize(OutputStream out, T t) throws SerializerException;

    /**
     * 反序列化
     *
     * @param in 输入流
     * @param type 类型
     * @return T
     * @throws SerializerException 序列化异常
     */
    <T> T deserialize(InputStream in, Type type) throws SerializerException;
}
