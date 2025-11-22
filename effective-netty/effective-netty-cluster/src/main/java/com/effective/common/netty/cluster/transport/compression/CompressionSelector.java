package com.effective.common.netty.cluster.transport.compression;


import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Compression Selector
 *
 * @date 2020/8/29
 */
@Slf4j

public class CompressionSelector {

    private static Map<String, Compression> compressionMap = new ConcurrentHashMap<>();

    private static Map<Byte, Compression> compressionCodeMap = new ConcurrentHashMap<>();


    public static void addHandler(Compression compression) {
        if (!compressionMap.containsKey(compression.getTypeName())) {
            compressionMap.putIfAbsent(compression.getTypeName(), compression);
            compressionCodeMap.putIfAbsent(compression.getTypeId(), compression);
        } else {
            log.error("SerializationSelector[{}] has duplicate handlers. This handler will be ignored (invalid)! handler class={}",
                    compression.getTypeName(), compression.getClass().getCanonicalName());
        }
    }

    public static Compression select(String contentType) {
        return compressionMap.get(contentType);
    }

    public static Compression select(Byte contentType) {
//        if (compressionCodeMap.isEmpty()) {
//            addHandler(new GzipCompression());
//            addHandler(new Lz4Compression());
//            addHandler(new Lz4FrameCompression());
//        }
        return compressionCodeMap.get(contentType);
    }

}
