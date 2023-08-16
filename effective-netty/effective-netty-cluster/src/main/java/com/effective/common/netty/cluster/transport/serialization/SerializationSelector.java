package com.effective.common.netty.cluster.transport.serialization;

import com.effective.common.netty.cluster.constants.GatewayConstants;
import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.BeansException;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.ApplicationContextAware;
//import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Serialization Selector
 *
 * @date 2020/3/8
 */
@Slf4j
//@Component
//public class SerializationSelector implements ApplicationContextAware {
public class SerializationSelector {

    private static Map<String, Serialization> serializationMap = new ConcurrentHashMap<>();

    private static Map<Byte, Serialization> serializationCodeMap = new ConcurrentHashMap<>();

//    @Override
//    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
//        if (applicationContext.getParent() == null ||
//                applicationContext.getParent().getParent() == null) {
//            Map<String, Serialization> beans = applicationContext.getBeansOfType(Serialization.class);
//            beans.forEach((beanName, commandHandler) -> addHandler(commandHandler));
//            log.info("{} {} Init handler finished, serialization handler count:{}",
//                    GatewayConstants.SYSTEM_NAME,
//                    GatewayConstants.SYSTEM_CORE,
//                    serializationMap.size());
//        }
//    }

    public static void addHandler(Serialization serialization) {
        serialization.getContentType().forEach(contentType -> {
            if (!serializationMap.containsKey(contentType)) {
                serializationMap.computeIfAbsent(contentType, s -> serialization);
                serializationCodeMap.computeIfAbsent(serialization.getContentCode(), b -> serialization);
            } else {
                log.error("SerializationSelector[{}] has duplicate handlers. This handler will be ignored (invalid)! handler class={}",
                        contentType, serialization.getClass().getCanonicalName());
            }
        });

    }

    public static Serialization select(String contentType) {
        return serializationMap.get(contentType);
    }

    public static Serialization select(Byte contentType) {
        return serializationCodeMap.get(contentType);
    }

}
