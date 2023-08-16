package com.effective.common.netty.cluster.utils;

import lombok.extern.slf4j.Slf4j;

/**
 * System Util
 *
 *
 * @date 2/2/2020
 */
@Slf4j
public class SystemUtil {

    /**
     * The server system is linux or not
     *
     * @return boolean
     */
    public static boolean isLinux() {
        String osName = System.getProperty("os.name");
        //log.info("{} The server running is the {} system.", SYSTEM_NAME,osName);
        return osName.toLowerCase().contains("linux");
    }
}
