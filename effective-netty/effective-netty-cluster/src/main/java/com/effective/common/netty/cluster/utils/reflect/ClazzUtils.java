package com.effective.common.netty.cluster.utils.reflect;

import com.effective.common.netty.cluster.utils.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;

import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class ClazzUtils {

    /**
     * 实例化接口实现类
     *
     * @param api         接口
     * @param packageName 包名
     * @param registerMap 注册容器
     */
    public static <T> void loadApiImplClass(Class<T> api, String packageName, Map<String, T> registerMap) {
        log.info("实例化接口实现类 接口:{},包名:{}", api.getSimpleName(), packageName);
        Reflections reflections = new Reflections(packageName);
        Set<Class<? extends T>> implClassSet = reflections.getSubTypesOf(api).stream().filter(x-> !x.isInterface() && !Modifier.isAbstract(x.getModifiers())).collect(Collectors.toSet());
        ObjectUtils.isTrue(0 < implClassSet.size(), implClassSet, set -> {
            for (Class<? extends T> x : set) {
                try {
                    log.info("实例化接口{}，实现类{}", api.getSimpleName(), x.getSimpleName());
                    String handlerName = x.getSimpleName();
                    T  t = x.newInstance();
                    registerMap.putIfAbsent(handlerName,t);
                } catch (Exception e) {
                    log.error("实例化接口实现类异常 {}", e.getMessage(), e);
                }
            }
        });
    }

}
