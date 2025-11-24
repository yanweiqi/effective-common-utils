package com.effective.common.netty.cluster.utils;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Slf4j
public class ObjectUtils {

    /**
     * jackson 转化工具
     */
    private static final ObjectMapper objectMapper = new ObjectMapper();


    @SuppressWarnings("unchecked")
    public static <T> T getInstance(T t) {
        try {
            Constructor<?> constructor = t.getClass().getConstructor();
            return (T) constructor.newInstance();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 对象转化
     *
     * @param sourceType 原始对象
     * @param targetType 目标对象
     * @param consumer   转化对象并执行函数
     */
    @SuppressWarnings("unchecked")
    public static <T, N> void isInstanceof(T sourceType, N targetType, Consumer<N> consumer) {
        if (Objects.nonNull(sourceType) && sourceType.getClass().isInstance(targetType)) {
            targetType = (N) sourceType;
            consumer.accept(targetType);
        }
    }

    /**
     * 对象不为空是进行赋值
     *
     * @param t 入参
     * @param c 对T进行处理函数
     */
    public static <T> void nonNull(T t, Consumer<T> c) {
        if (Objects.nonNull(t)) {
            c.accept(t);
        }
    }

    /**
     * 对T进行处理函数，支持空对象和空字符串
     *
     * @param t 入参
     * @param c 处理函数
     */
    public static <T> void nonNullPlus(T t, Consumer<T> c) {
        if (Objects.nonNull(t)) {
            if (t.getClass().getName().equals(String.class.getName())) {
                if (StringUtils.isNotBlank((String) t)) {
                    c.accept(t);
                }
            }
            c.accept(t);
        }
    }

    /**
     * 对象不为空是进行赋值
     *
     * @param t 入参
     * @param c 对T进行处理函数
     */
    public static <T> void isNull(T t, Consumer<T> c) {
        if (Objects.isNull(t)) {
            c.accept(t);
        }
    }


    /**
     * @param t
     * @param s
     * @param <T>
     */
    public static <T> T getNullProperty(T t, Supplier<T> s) {
        if (Objects.isNull(t)) {
            s.get();
        }
        return t;
    }

    /**
     * @param t 赋值
     * @param r 接收t的赋值
     * @param f 返回R的函数
     * @return
     */
    public static <T, R> R getNonNullProperty(T t, R r, Function<T, R> f) {
        if (Objects.nonNull(t) && Objects.nonNull(r)) {
            return f.apply(t);
        }
        return null;
    }

    /**
     * 字符串转数组
     *
     * @param json          字符串数组
     * @param typeReference 参考对象
     * @param <T>           泛型
     * @return List
     */
    public static <T> List<T> nonNullConvert(String json, TypeReference<List<T>> typeReference) {
        List<T> list = new ArrayList<>();
        try {
            list = objectMapper.readValue(json, typeReference);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return list;
    }

    /**
     * 条件成功执行函数
     *
     * @param b 布尔参数
     * @param t 消费参数
     * @param c 消费函数
     */
    public static <T> void isTrue(Boolean b, T t, Consumer<T> c) {
        if (Objects.nonNull(b) && b && Objects.nonNull(t)) {
            c.accept(t);
        }
    }

    /**
     * 非空集合处理函数
     *
     * @param list 消费参数
     * @param c    消费函数
     */
    public static <T> void isNotEmpty(List<T> list, Consumer<List<T>> c) {
        if (Objects.nonNull(list) && !list.isEmpty()) {
            c.accept(list);
        }
    }

    /**
     * 空集合处理函数
     *
     * @param list 消费参数
     * @param c    消费函数
     */
    public static <T> void isEmpty(List<T> list, Consumer<List<T>> c) {
        if (Objects.isNull(list) || list.isEmpty()) {
            c.accept(list);
        }
    }

    /**
     * 返回json
     *
     * @param t 参数
     * @return string json
     */
    public static <T> String toJson(T t) {
        if (Objects.nonNull(t)) {
            try {
                return objectMapper.writeValueAsString(t);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        return "";
    }

    /**
     * 对象转化
     *
     * @param t             入参
     * @param typeReference 泛型出参
     * @return
     */
    public static <R, T> R toObject(T t, TypeReference<R> typeReference) {
        if (Objects.nonNull(t) && Objects.nonNull(typeReference)) {
            try {
                return objectMapper.convertValue(t, typeReference);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        return null;
    }

    /**
     * json转化对象
     *
     * @param json          入参
     * @param typeReference 泛型出参
     * @return
     */
    public static <R> R toObject(String json, TypeReference<R> typeReference) {
        if (Objects.nonNull(json) && Objects.nonNull(typeReference)) {
            try {
                return objectMapper.readValue(json, typeReference);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        return null;
    }

    /**
     * @param t 入参
     * @param s 消费者
     * @return this
     */
    public <T> T orElse(T t, Supplier<T> s) {
        if (Objects.isNull(t)) {
            return s.get();
        } else {
            if (t.getClass().getName().equals(String.class.getName())) {
                if (StringUtils.isBlank((String) t)) {
                    return s.get();
                }
            }
        }
        return t;
    }

}
